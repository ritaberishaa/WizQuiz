# WizQuiz

WizQuiz është një aplikacion kuizi për Android që u mundëson studentëve të testojnë njohuritë në programim në mënyrë offline, me rezultat të menjëhershëm dhe feedback përmes komenteve.

## Veçoritë

| Kategoria            | Përshkrimi                                                      |
|----------------------|-----------------------------------------------------------------|
| Kuiz offline         | Pyetje të ruajtura lokalisht, nuk kërkohet internet              |
| Autentikim i sigurt  | Hash BCrypt për fjalëkalimet, rikuperim me OTP 6-shifror         |
| Rezultate të çastit  | Dialog përfundimtar me pikët dhe opsion për të rifilluar kuizin |
| Komente              | CRUD komentesh të ruajtura në SQLite                            |
| UI Material Design   | Butona, input-e dhe animacione në përputhje me udhëzimet e Google|

## Kërkesat

- Android Studio Arctic Fox (ose më i ri)
- JDK 17
- Pajisje ose emulator me Android 8.0+

## Siguria
- BCrypt me salt për çdo fjalëkalim
- OTP 6-shifror ruhet me email dhe afat skadimi 5 minuta
- OTP fshihet pas përdorimit ose skadimit

## Instalimi dhe ekzekutimi

```bash
git clone https://github.com/username/WizQuiz.git
cd WizQuiz
# Hap projektin në Android Studio
# Lejo Gradle të sinkronizohet dhe shtyp Run

app/
├── java/com/example/wizquiz/
│   ├── WelcomeActivity.java       # Ekrani hyrës
│   ├── LoginActivity.java         # Autentikimi
│   ├── SignUpActivity.java        # Regjistrimi
│   ├── ForgotPasswordActivity.java
│   ├── VerifyCodeActivity.java
│   ├── ResetPasswordActivity.java
│   ├── HomeActivity.java          # Kuizi
│   ├── CommentActivity.java       # Komentet
│   ├── DatabaseHelper.java        # SQLite: users & otp
│   ├── CommentDatabaseHelper.java # SQLite: comments
│   └── OTPEmailSender.java        # JavaMail
└── res/layout/                    # Skedarët e ndërfaqes
