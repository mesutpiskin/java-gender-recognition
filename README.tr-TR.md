[English](./README.md) | Türkçe


<div align="center">

<h1 align="center">JavaCV Kütüphanesi Üzerinde ile Derin Sinir Ağı (DNN) Kullanarak Cinsiyet Tespiti</h1>

<img width="200" src="./static/logo.png"/>

</div>

Bu örnek; girdi olarak verilen görüntü üzerindeki yüzleri yakalayıp cinsiyet tahminlemesi yapar. Bunu yaparken eğitilmiş caffe modelini, OpenCV'nin Java wrapper'i olan JavaCV'nin "Derin Sinir Ağı" anlamına gelen DNN paketi üzerinde sınıflandırıcı  oluşturarak kullanır. Eğitilmiş model *src/main/resources/* dizini altındadır.

### JavaCV Nedir?

Java teknolojisi çatısı altında kullanmak için geliştirilmiş bir wrapper’dır. **OpenCV** kütüphanesini referans alır ve Java içerisinde C++ yazımında (syntax) uygulama geliştirmeyi destekler. Sıklıkla kullanılan bir çok algoritmayı kullanılabilirlik açısından kolaylaştırmışlardır. Sadece OpenCV değil FFmpeg, libdc1394, PGR FlyCapture, OpenKinect, videoInput, ARToolKitPlus, ve  flandmark gibi kütüphaneleride kullanmaktadır. Bytedeco tarafından açık kaynak kod olarak geliştirilmektedir ve güncelliğini devam ettiren bir kütüphanedir topluluk desteği bulunmaktadır. 

### JavaCV Maven

```
 <dependencies>
        <dependency>
            <groupId>org.bytedeco</groupId>
            <artifactId>javacv</artifactId>
            <version>1.2</version>
        </dependency>  

        <dependency>
            <groupId>net.coobird</groupId>
            <artifactId>thumbnailator</artifactId>
            <version>0.4.8</version>
        </dependency>

    </dependencies>
```


### Nasıl Çalıştırılır?

- Kaynak kodu indirin
```bash
git clone https://github.com/mesutpiskin/GenderClassification.git
```
- IDE'niz ile Java Maven projesi oluşturun ve kaynak kodlar ile kaynakları (resources) içe aktarın.
- Maven POM.XML dosyasına yukarıdaki maven bytcode-javacv referansını ekleyin.
- Projeyi derleyin ve UICamera.java sınıfını çalıştırın.
- Uygulama varsayılan kamera aygıtı ile çalışmaya başlayacaktır. Sistemde varsayılan bir kamera aygıtı yoksa usb kamera takarak test edebilirsiniz.


### Sonuç

<img width="400" src="./static/ss.png"/>