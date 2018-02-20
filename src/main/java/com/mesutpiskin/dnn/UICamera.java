package com.mesutpiskin.dnn;

import net.coobird.thumbnailator.Thumbnails;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.CvRect;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_objdetect;
import org.bytedeco.javacv.*;
import org.bytedeco.javacv.Frame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import static org.bytedeco.javacpp.helper.opencv_objdetect.cvHaarDetectObjects;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_objdetect.CV_HAAR_DO_CANNY_PRUNING;

public class UICamera {
    private OpenCVFrameGrabber openCVFrameGrabber;
    private OpenCVFrameConverter.ToMat openCVFrameConverter = new OpenCVFrameConverter.ToMat();
    private volatile boolean running = false;
    private static Java2DFrameConverter java2DFrameConverter = new Java2DFrameConverter();
    private DNNClassification classifier = new DNNClassification();
    private JFrame window;
    private JPanel videoPanel;
    private opencv_objdetect.CvHaarClassifierCascade cvHaarClassifierCascade;
    private opencv_core.CvMemStorage cvMemStorage;
    private OpenCVFrameConverter.ToIplImage toIplImage;
    private OpenCVFrameConverter.ToMat toMatConverterCascade;

    public UICamera() {
        window = new JFrame();
        videoPanel = new JPanel();
        window.setLayout(new BorderLayout());
        window.setSize(new Dimension(800, 700));
        window.add(videoPanel, BorderLayout.CENTER);
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                stop();
            }
        });
        toIplImage = new OpenCVFrameConverter.ToIplImage();
        toMatConverterCascade = new OpenCVFrameConverter.ToMat();
        try {
            File haarCascade = new File(this.getClass().getResource("/files/haarcascade_frontalface_alt.xml").toURI());
            cvHaarClassifierCascade = new opencv_objdetect.CvHaarClassifierCascade(cvLoad(haarCascade.getAbsolutePath()));
        } catch (Exception e) {
            throw new IllegalStateException("Error", e);
        }
        cvMemStorage = opencv_core.CvMemStorage.create();
    }

    public void run() {
        //Windows için usb kamerayı başlatır
        openCVFrameGrabber = new OpenCVFrameGrabber(0); //0 varsayılan usb kamera
        openCVFrameGrabber.setFormat("dshow");
        openCVFrameGrabber.setImageWidth(800);
        openCVFrameGrabber.setImageHeight(700);
        try {
            openCVFrameGrabber.start();
        } catch (FrameGrabber.Exception e) {
            throw new RuntimeException("Error", e);
        }
        SwingUtilities.invokeLater(() -> {
            window.setVisible(true);
        });
        process();
    }

    private void process() {
        running = true;
        while (running) {
            try {
                final Frame frame = openCVFrameGrabber.grab();
                Map<CvRect, Mat> detectedFaces = detect(frame);
                Mat mat = openCVFrameConverter.convert(frame);

                detectedFaces.entrySet().forEach(rectMatEntry -> {

                    String gender = classifier.classification(rectMatEntry.getValue(), frame);
                    String caption = "Cinsiyet (Gender):" + gender + "";
                    rectangle(mat, new opencv_core.Point(rectMatEntry.getKey().x(), rectMatEntry.getKey().y()),
                            new opencv_core.Point(rectMatEntry.getKey().width() + rectMatEntry.getKey().x(), rectMatEntry.getKey().height() + rectMatEntry.getKey().y()),
                            Scalar.GREEN, 1, CV_AA, 0);
                    int posX = Math.max(rectMatEntry.getKey().x() - 10, 0);
                    int posY = Math.max(rectMatEntry.getKey().y() - 10, 0);
                    putText(mat, caption, new opencv_core.Point(posX, posY), CV_FONT_HERSHEY_PLAIN, 1.0,
                            new Scalar(255, 0, 0, 1.0));
                });
                Frame processedFrame = openCVFrameConverter.convert(mat);
                Graphics graphics = videoPanel.getGraphics();
                BufferedImage resizedImage = getResizedBufferedImage(processedFrame, videoPanel);
                SwingUtilities.invokeLater(() -> {
                    graphics.drawImage(resizedImage, 0, 0, videoPanel);
                });
            }catch (Exception e) {
            }
        }
    }

    public void stop() {
        running = false;
        try {
            openCVFrameGrabber.release();
            openCVFrameGrabber.stop();
        } catch (FrameGrabber.Exception e) {
        }

        window.dispose();
    }

    public static void main(String[] args) {
        UICamera UICamera = new UICamera();
        new Thread(UICamera::run).start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            UICamera.stop();
        }));
        try {
            Thread.currentThread().join();
        } catch (Exception ex) {
        }
    }
    //Jframe için matrisleri image'e dönüştür
    //bkz. http://mesutpiskin.com/blog/opencv-javada-resim-goruntuleme-imshow-metodu.html
    public static BufferedImage getResizedBufferedImage(Frame frame, JPanel videoPanel) {
        BufferedImage resizedImage = null;
        try {
            resizedImage = Thumbnails.of(java2DFrameConverter.getBufferedImage(frame))
                    .size(videoPanel.getWidth(), videoPanel.getHeight())
                    .asBufferedImage();
        } catch (Exception e) {
        }
        return resizedImage;
    }

    /*
     Cascade sınıflandırıcısı yüz tespiti
     https://www.youtube.com/watch?v=Cqtzwhq3IuA
     http://mesutpiskin.com/blog/321.html
     */
    public Map<CvRect, Mat> detect(Frame frame) {
        Map<CvRect, Mat> detectedFaces = new HashMap<>();
        opencv_core.IplImage iplImage = toIplImage.convert(frame);
        opencv_core.CvSeq detectObjects = cvHaarDetectObjects(iplImage, cvHaarClassifierCascade, cvMemStorage, 1.5, 3, CV_HAAR_DO_CANNY_PRUNING);
        Mat matImage = toMatConverterCascade.convert(frame);
        int numberOfPeople = detectObjects.total();
      /*  Birden çok yüzler için (multiple face)
        for (int i = 0; i < numberOfPeople; i++) {
            CvRect rect = new CvRect(cvGetSeqElem(detectObjects, i));
            Mat croppedMat = matImage.apply(new Rect(rect.x(), rect.y(), rect.width(), rect.height()));
            detectedFaces.put(rect, croppedMat);
        }*/
        CvRect rect = new CvRect(cvGetSeqElem(detectObjects, 1));
        Mat croppedMat = matImage.apply(new Rect(rect.x(), rect.y(), rect.width(), rect.height()));
        detectedFaces.put(rect, croppedMat);
        return detectedFaces;
    }
}
