package com.ays.auth.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AysConfigurationParameters {

    AYS("AYS"),
    LOGIN_MAX_TRY_COUNT("3"),
    ACCESS_TOKEN_EXPIRE_MINUTE("120"),
    REFRESH_TOKEN_EXPIRE_DAY("1"),
    AUTHENTICATION_TOKEN_PRIVATE_KEY("""
            -----BEGIN PRIVATE KEY-----
            MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCcxnbevWSMsZ0T
            q2q/wNvZC9FeFZ6NkhtQfnn1VcV9cjJ2rlKUUoFrCqPqn44nj32zTO+FfD0oXuWm
            qNvGEC/2+fyjr/DyGEy6o/LV0F/baByWqHVZejS6lVzEQdZeelsvJ1mH/1iQUzPt
            SpREL7QsxuV7iiNHeTSHHNI0st5a7dXJm2nGWr9PU0FUu+mjmzCJddsDM6j5U8lO
            paTBENP1WvzYSbRfZUp6L99KLw1kGNk9XLc2vFbRNvv2XBd+lxSyTUHSxZvuqo6v
            LQEzRyZUf3EdDXxCY0XUY8Wf6+4TfM/Mlinm9V0CauVPP+9YKSEt9l9uePuB+bAs
            NmxxNLeNAgMBAAECggEBAIuZeb3LXZ6ehaU/LXYEEH/LsyoZDC8529KoXIbmUk3r
            Ax6FCYmDkjQzrFQ+sYFul1qydlIhl/+7yLjHgbNzNJKydZF4GJAqrSBA3vofa0vf
            AB3zcVM0q4E9gPZNPcqxH0L0wheEe2NAg2uXzkCNTd3VWgDQt6tsNSbethsw7yh9
            Wg56y6utvHBcoG2v6ziJHq8HJpU6+8r7Ly3Ht3YWqLkBTSw8q4rR6VqS8dtPBCZT
            A3TmIcE3kHbop85uxM8lEltGAPJ/OONqkYBSsuN/Y9uaJkZn3bbx6YwRwgsvxpvW
            ast3VpcI6NK/BjgBV3dbiokSyrHa3L1deIEdF7TqLWECgYEA41kDM3EbSKCFHUrK
            AN0cIVoJptgLxSYvPwwI8/QqxJlwQRM/TumAHB2KuOMHdFpZ1XI0Q/ZyoMZXhOmc
            nqIf21hx0POgF6bDech2Um1xWcyDtWwZRckipBVfkLsVrQScBMocAVtfVr+OmQgF
            8WJOMOSzlqEm7ZUx7hfuBtCTJpkCgYEAsIiNLuUy3T2/oVQg40kW8TbLD6NZ9w5/
            AhgnHEKVJX3sWNc9+iludMoqGT7v580p8Z6XCvDCJcEigna+U3tSHAzT86GGSIRN
            aaZu3Zi3PS8cfWjVoKcCIqtutWn6Erw8NyGmLfl21cIHqDvpGPd+8HV90GbJHRtK
            43/n3c24FRUCgYAoDLE0qbMtuyFYBKnLNyhSc8MJgjuokk52Y89b8mJqWcFdpV9c
            rmOkEEw0v8G7bIMwx7qPUmXlDquyPVka1OKZyF3cel50bCs5U+gMIz15nT6CgjV9
            hq40i5NXMq3h2dMGQPhoAMmtxXcAXFp3qGKv2EoOufralZLDJ7IFv0582QKBgQCf
            QaE0hDq90r31GcNNqBtVYtwP8Irx0ZcM2aM24G5S02YLw6o663OlUthlzBx73t+P
            BUY37XkIvFKYpe0PvPoXKg9wA/DzrL91p1ru+J2eSckG7jOC6geIJWYLrV6X1k7w
            YZ/ca+gj8RYdVGb2Shivbx8gigm3A28tC6+urq75/QKBgFQrpWLFkWQKvLmO2nIh
            Z75hxSU0Mto0Id2XMtXKOQZUQ5il12Y0jwdeSe4R6x9MFu+6QmaQ0m5SOy8zUVm6
            wmh1dws7WpRlzZBvHCkmLc4Th21O85sm82n0TWQpj4r4QrwXJFONK3OA5ZYSlLZK
            VFikfnNRHnQPgmSPzc+XkCIR
            -----END PRIVATE KEY-----
            """),
    AUTHENTICATION_TOKEN_PUBLIC_KEY("""
            -----BEGIN PUBLIC KEY-----
            MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnMZ23r1kjLGdE6tqv8Db
            2QvRXhWejZIbUH559VXFfXIydq5SlFKBawqj6p+OJ499s0zvhXw9KF7lpqjbxhAv
            9vn8o6/w8hhMuqPy1dBf22gclqh1WXo0upVcxEHWXnpbLydZh/9YkFMz7UqURC+0
            LMble4ojR3k0hxzSNLLeWu3VyZtpxlq/T1NBVLvpo5swiXXbAzOo+VPJTqWkwRDT
            9Vr82Em0X2VKei/fSi8NZBjZPVy3NrxW0Tb79lwXfpcUsk1B0sWb7qqOry0BM0cm
            VH9xHQ18QmNF1GPFn+vuE3zPzJYp5vVdAmrlTz/vWCkhLfZfbnj7gfmwLDZscTS3
            jQIDAQAB
            -----END PUBLIC KEY-----
            """);

    private final String defaultValue;
}
