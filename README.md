# JavaCV ile Cinsiyet Tespiti (DNN)
Cinsiyet tespiti bir sınıflandırma problemidir. Bazılarınız bunu denemiş veya okumuş olabilir. Örnekte, cinsiyeti tanımak için farklı bir yaklaşım kullanacağım. Bu yöntem iki  araştırmacı tarafından Gil Levi ve Tal Hassner tarafından 2015 yılında tanıtıldı. Bu örnekte eğitilmiş modelleri ve OpenCV'nin "Derin Sinir Ağı" anlamına gelen DNN paketini kullandım.


### Maven Bağımlılıkları
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

### Caffe Modeli

https://gist.github.com/GilLevi/c9e99062283c719c03de


### Ek bağlantılar
JavaCV Nedir? http://mesutpiskin.com/blog/javacv-nedir.html

OpenCV Wrappers http://mesutpiskin.com/blog/opencv-wrappers.html
