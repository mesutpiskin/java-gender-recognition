package com.mesutpiskin.cnn;

import org.bytedeco.javacpp.indexer.Indexer;
import org.bytedeco.javacv.Frame;
import java.io.File;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_dnn.*;
import static org.bytedeco.javacpp.opencv_imgproc.resize;


public class CNNClassification {
    private Net net;
    public CNNClassification() {
        try {
            net = new Net();
            File protobuf = new File(getClass().getResource("/files/deploy_gendernet.prototxt").toURI());
            File caffeModel = new File(getClass().getResource("/files/gender_net.caffemodel").toURI());
            Importer importer = createCaffeImporter(protobuf.getAbsolutePath(), caffeModel.getAbsolutePath());
            importer.populateNet(net);
            importer.close();
        } catch (Exception e) {
           //error management
        }
    }

    public String classification(Mat face, Frame frame) {
        try {
            Mat croppedMat = new Mat();
            resize(face, croppedMat, new Size(256, 256));
            normalize(croppedMat, croppedMat, 0, Math.pow(2, frame.imageDepth), NORM_MINMAX, -1, null);
            Blob inputBlob = new Blob(croppedMat);
            net.setBlob(".data", inputBlob);
            net.forward();
            Blob prob = net.getBlob("prob");
            Indexer indexer = prob.matRefConst().createIndexer();
            if (indexer.getDouble(0, 0) > indexer.getDouble(0, 1)) {
                return "ERKEK (MALE)";
            } else {
                return "KADIN (FEMALE)";
            }
        } catch (Exception e) {
            //error management
        }
        return "NULL";
    }

}
