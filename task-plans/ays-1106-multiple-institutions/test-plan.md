# AYS-1106 — Test Planı

## Senaryolar

| ID | Tür | Senaryo / doğrulama | Doğrulama |
| --- | --- | --- | --- |
| TC-01 | Geriye uyumluluk | Eski tekil `institutionId` ile admin başvurusu oluşturulabilmeli. | Admin registration servis ve controller testleri |
| TC-02 | Çoklu kurum | `institutionIds` dizisiyle başvuru açılınca tüm kurumlar kaydedilip tekrar okunmalı. | `AdminRegistrationApplicationEndToEndTest` |
| TC-03 | Tamamlama | Çok kurumlu başvuru tamamlanınca kullanıcı tüm kurumlara atanmalı. | `AdminRegistrationApplicationEndToEndTest` |
| TC-04 | Rol ataması | Tamamlama sırasında her seçili kurumun mevcut aktif admin rolleri kullanıcıya atanmalı; rol yoksa o kurum için admin rolü oluşturulmalı. | `AdminRegistrationApplicationEndToEndTest` |
| TC-05 | Kurum doğrulama | Listelenen kurumlardan biri yok veya aktif değilse başvuru reddedilmeli. | Servis testleri |
| TC-06 | Veri geçişi | Eski başvuruların `INSTITUTION_ID` değeri yeni ilişki tablosuna taşınmalı. | Liquibase geçişi ve test DB doğrulaması |
| TC-07 | Kullanıcı ilişkileri | Çok kurumlu kullanıcının kurumları veritabanından tekrar okunduğunda korunmalı. | `AysAuthEndToEndTest` |
| TC-08 | Dokümantasyon | Postman admin başvurusu `institutionIds` dizisini kullanmalı. | Koleksiyon JSON kontrolü |

## Çalıştırılacak kontroller

```bash
./mvnw -Dtest=AdminRegistrationApplicationEndToEndTest,AdminRegistrationApplicationServiceImplTest,AdminRegistrationApplicationControllerTest,AysAuthEndToEndTest test
```

Migration doğrulaması test veritabanında çalıştırılmalı; entegrasyon testi
başarısı üretim verisinin migration doğrulaması olarak kabul edilmemelidir.
