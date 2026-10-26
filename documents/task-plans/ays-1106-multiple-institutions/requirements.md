# AYS-1106 — Kullanıcıların Birden Fazla Institution'a Sahip Olabilmesi

## Amaç

Kullanıcının birden fazla kurumla ilişkilendirilebilmesini ve admin kayıt akışının
bu ilişkileri uçtan uca taşımasını sağlamak; tek kurumlu mevcut istemcileri
geriye uyumlu tutmak.

## Gereksinimler

| ID | Gereksinim |
| --- | --- |
| R1 | Kullanıcı-kurum ilişkisi `AYS_USER_INSTITUTION_RELATION` tablosunda çoktan-çoğa olarak tutulur. |
| R2 | Admin kayıt başvurusu `institutionIds` listesiyle birden fazla aktif kurumu kabul eder. |
| R3 | Admin kayıt başvurusunun kurumları ayrı ilişki tablosunda saklanır; mevcut başvuruların `INSTITUTION_ID` değeri yeni tabloya backfill edilir. |
| R4 | Admin başvurusu tamamlandığında kullanıcı bütün seçili kurumlara atanır ve her kurum için geçerli admin rolleri eklenir. |
| R5 | Tekil `institutionId` ile çalışan mevcut admin kayıt istemcileri desteklenmeye devam eder. |
| R6 | Seçilen tüm kurumlar var ve aktif olmalıdır; geçersiz kurum için başvuru oluşturulmamalıdır. |
| R7 | Postman örneği `institutionIds` JSON dizisini göstermelidir. |

## Kapsam ve notlar

- `institutionId` eski istemcilerle uyumluluk için opsiyonel legacy alan olarak
  kalır. Çoklu kurumlu istemciler `institutionIds` alanını kullanır.
- Postman örneğinde `institutionIds` dizisi bulunur; test kurulumundaki tek
  kurum değişkeni de tek elemanlı dizi şeklinde gönderilir.

## Kabul ölçütleri

1. Birden fazla geçerli kurum kimliğiyle admin kayıt başvurusu oluşturulabilir.
2. Başvuru yeniden okunduğunda tüm kurumlar korunur.
3. Başvuru tamamlanınca oluşturulan kullanıcı tüm kurumlara ve bu kurumların
   admin rollerine atanır.
4. Eski tekil `institutionId` isteği başarıyla çalışmayı sürdürür.
5. Postman koleksiyonu güncel isteği ve çoklu kurum alanını gösterir.
