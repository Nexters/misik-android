# Misik - AI Receipt Review App
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.0%2B-purple?logo=kotlin)](https://kotlinlang.org)
[![api](https://img.shields.io/badge/API-28%2B-green?logo=android)](https://developer.android.com/distribute/best-practices/develop/target-sdk)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)

📸 Snap a receipt and let AI generate restaurant reviews instantly!✨🚀

*OCR-based receipt scanning & data extraction*

*AI-powered automatic review generation using LLM*

*Intuitive UI & easy sharing functionality*

<div>
    <a href="https://play.google.com/store/apps/details?id=com.nexters.misik">
        <img src="https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png" height="70">
    </a>
    <img width="6%" src="https://github.com/user-attachments/assets/66f5551b-2ba6-43f9-9928-3ec2794c44e8">
</div>


## Screenshots
<img width=30% src="https://github.com/user-attachments/assets/6be8235f-80fa-484b-9678-0f791b1d8fe1"> 
<img width=15% src="https://github.com/user-attachments/assets/e696eedc-1aa6-4971-ae25-df14a8f5cb28"> 
<img width=15% src="https://github.com/user-attachments/assets/afc2e6ea-e7f1-423d-827a-bf6db731e2c5">
<img width=15% src="https://github.com/user-attachments/assets/1759fafe-f9f0-494f-b3b2-8cc7eb2e9104">
<img width=15% src="https://github.com/user-attachments/assets/9aa1fb57-ad67-431b-ab4e-c4b9eb6f97cd">

## Package Structure
```
📦 misik-android
├── 📂 build-logic                # Gradle 빌드 설정 관련 모듈
├── 📂 app                        # Android 메인 애플리케이션 모듈
├── 📂 core                       # 핵심 로직을 담당하는 공통 모듈
│   ├── 📂 core-data              # 데이터 레이어 (Repository, DataSource 등)
│   ├── 📂 core-domain            # 비즈니스 로직 및 UseCase 모듈
│   ├── 📂 core-network           # 네트워크 관련 모듈 (API, Retrofit 등)
│   ├── 📂 core-ui                # 공통 UI 컴포넌트, 테마, 디자인 시스템
├── 📂 feature                    # 개별 기능을 담당하는 Feature 모듈
│   ├── 📂 feature-webview        # 웹뷰 관련 기능 모듈
│   ├── 📂 feature-preview        # 이미지 프리뷰 및 OCR 연결 모듈
├── 📂 ocr                        # OCR 처리 관련 모듈 (MLKit, Cloud Vision API 등)
```

## Stack & Libraries
- Kotlin & Coroutine
- Jetpack Compose
- Cloud Vision & MLkit
- Hilt
- Retrofit2, OkHttp3
- Coil
- Lottie


## Contributors

|[freeskyES](https://github.com/freeskyES)|[sxunea](https://github.com/sxunea)|
|:---:|:---:|
|<img src="https://avatars.githubusercontent.com/u/19375957?v=4" width="180">|<img src="https://avatars.githubusercontent.com/u/81434152?v=4" width="180">|
|  Eunsong Cheon | Hyeseon Baek | 


</br>

## Other Repositories

| Part | Repository |
| --- | --- |
| iOS | [Link](https://github.com/Nexters/misik-ios)
| FE | [Link](https://github.com/Nexters/misik-web)
| BE | [Link](https://github.com/Nexters/misik-api)

</br>

## Hackathon
<img width="300" alt="image" src="https://github.com/user-attachments/assets/3686844c-9df4-4eaa-9c34-2d87c16f4b45" />
