Kasım ADALAN'ın Eğitmenliğindeki Kotlin Bootcampinin bitirme projesi.
İnternet üzerinden ilgili veritabanlarıyla iletişim kurmaya sağlayan bir APİ hizmeti vasıtasıyla veri çekere bir yemek sipariş uygulama prototipi. 
Uygulamada 
AnasayfaFragment, DetayFragment, SepetFragment olarak 3 sayfamız bulunmaktadır. 
AnasayfaFragment içerisinde arama barıyla ilgili aramalar yapılıp, istenilen ürün sepete eklenebilir veya detayları görüntülenebilir.
DetayFragment içerisinde detayı görüntülenen ürünün ilgili API Servisi tarafından sağlanan verileri ekranda görüntülenir ve çoklu sayıda sepete ekleme yapılabilir.
SepetFragment içerisinde sepete eklenen ürünler listelenir.

SepetFragment ile ilgili söylenmemiş 2 problem olduğu söylenmişti. Kendimce tespit ettiğim 2 problem ; 
1.) Sepete eklenen ürünlerin her biri farklı satırlarda eklenip farklı kalemden ürünmüş gibi görünüyor.
2.) Sepetteki tüm ürünleri sildikten sonra silecek ürün kalmazsa son silinen ürün kalıyor orda. Eğer sayfa değiştirip geri gelinirse kayboluyor oradan. 

NOT : AnasayfaFragment içerisinde listeleme işlemi APİ'dan gelen veriler listelenmektedir. Ve bu sayfada sepete ekleme işlemi de api üstündeki Sepet'e eklenmektedir. Sepet sayfası ise APİ'dan gelen sepet verisini listelemektedir. Yani local bir depolama kullanılmamıştır.
Merhaba, isim
Adres
Ev 

Gibi kısımlar oluşturulmuş fakat boş bırakılmıştır. Eğer ki bir login sistemi tasarlanırsa oralar login olan kullanıcının database'den gelen verilerine göre dinamik olarak değiştirilebilir.
