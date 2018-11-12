# JavaCV ile Cinsiyet Tespiti (DNN)
Cinsiyet tespiti bir sınıflandırma problemidir. Bazılarınız bunu denemiş veya okumuş olabilir. Örnekte, cinsiyeti tanımak için farklı bir yaklaşım kullanacağım. Bu yöntem iki  araştırmacı tarafından Gil Levi ve Tal Hassner tarafından 2015 yılında tanıtıldı. 


Bu örnek; Eğitilmiş caffe modelini, OpenCV'nin Java wrapper'i olan JavaCV ve OpenCV'nin "Derin Sinir Ağı" anlamına gelen DNN paketi ile sınıflandırıcı oluşturmaktadır.

Eğitilmiş model göz atmak isterseniz https://gist.github.com/GilLevi/c9e99062283c719c03de

### Nasıl Çalıştırılır?

- Kaynak kodu indirin
```bash
git clone https://github.com/mesutpiskin/GenderClassification.git
```
- IDE'niz ile Java Maven projesi oluşturun ve kaynak kodlar ile kaynakları (resources) içe aktarın.
- Maven POM.XML dosyasına aşağıdakş bytcode-javacv referansını ekleyin.

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
- Projeyi derleyin ve UICamera.java sınıfını çalıştırın.
- Uygulama varsayılan kamera aygıtı ile çalışmaya başlayacaktır. Sistemde varsayılan bir kamera aygıtı yoksa usb kamera takarak test edebilirsiniz.
