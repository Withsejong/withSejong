# withSejong
> "세종대학교 학우들을 위한 중고물품 거래 플랫폼 세종끼리"

## 📖 프로젝트 개요 (Project Overview)

**withSejong**은 세종대학교 학생들이 서로 소통하고 정보를 공유하며, 중고 거래 등을 편리하게 할 수 있는 안드로이드 애플리케이션입니다.
**XML Layout**과 **ViewBinding**을 활용하여 직관적인 UI를 제공하며, **MVVM 아키텍처**를 기반으로 유지보수성과 확장성을 고려하여 개발되었습니다.

세종대 학생 인증을 통해 신뢰할 수 있는 커뮤니티 환경을 제공합니다.

### 🎯 개발 목표
*   **신뢰성**: 세종대 학생 인증을 통한 안전한 커뮤니티 조성
*   **편의성**: 직관적인 UI와 게시판, 채팅 기능을 통한 원활한 소통
*   **실시간성**: FCM 및 실시간 채팅을 통한 빠른 정보 전달

---

## 🚀 핵심 기능 (Key Features)

### 1. 🔐 학생 인증 및 회원가입 (Auth & Verification)
세종대학교 포털 인증을 통해 재학생임을 확인하고 가입할 수 있습니다.
*   **세종대 인증**: 실제 학생만 이용 가능한 클린한 커뮤니티를 지향합니다.
*   **계정 관리**: 닉네임 중복 확인, 비밀번호 변경, 회원 탈퇴 등의 기능을 제공합니다.

### 2. 📝 게시판 및 중고 거래 (Board & Marketplace)
자유롭게 게시글을 작성하고, 물품을 거래할 수 있는 게시판 기능을 제공합니다.
*   **게시글 작성**: 이미지 첨부 및 태그 기능을 지원합니다.
*   **검색 및 필터**: 키워드 및 태그를 통해 원하는 정보를 빠르게 찾을 수 있습니다.
*   **끌어올리기**: 자신의 게시글을 상단으로 노출시키는 기능을 제공합니다.
*   **판매 내역**: 자신의 거래 내역을 한눈에 확인할 수 있습니다.

### 3. 💬 실시간 채팅 (Real-time Chat)
게시글 작성자나 다른 학우들과 실시간으로 대화를 나눌 수 있습니다.
*   **채팅방 생성**: 1:1 대화 및 그룹 대화방을 생성할 수 있습니다.
*   **실시간 메시지**: 지연 없는 빠른 메시지 전송을 지원합니다.
*   **채팅 목록**: 참여 중인 모든 채팅방을 관리할 수 있습니다.

### 4. 🔔 알림 및 신고 (Notification & Report)
*   **FCM 알림**: 채팅 메시지나 중요한 알림을 실시간으로 받아볼 수 있습니다.
*   **신고 기능**: 부적절한 사용자나 게시글을 신고하여 쾌적한 환경을 유지합니다.

---

## 🛠️ 시스템 구성도 (System Architecture)

### Architecture Pattern: MVVM (Model-View-ViewModel)

*   **View (Activity/Fragment)**: XML Layout으로 구성된 UI 레이어입니다. ViewBinding을 통해 UI 요소에 접근하고, ViewModel의 State를 구독하여 화면을 갱신합니다.
*   **ViewModel**: UI 상태를 관리하고, Repository를 통해 데이터를 요청하며 비즈니스 로직을 처리합니다.
*   **Model (Repository & Data Source)**:
    *   **Retrofit**: 백엔드 서버와의 REST API 통신을 담당합니다.
    *   **Data Class**: 서버로부터 받은 JSON 데이터를 객체로 매핑합니다.

---

## 💻 기술 스택 (Tech Stack)

| Category | Technology |
| :--- | :--- |
| **Language** | Kotlin |
| **Architecture** | MVVM (Model-View-ViewModel) |
| **UI Framework** | XML Layout, ViewBinding |
| **Network** | Retrofit2, OkHttp |
| **Async** | Coroutines, Thread |
| **Real-time** | Stomp (WebSocket), FCM |

---

## 📥 설치 및 실행 방법 (Installation & Setup)

이 프로젝트는 Android Studio에서 빌드 및 실행할 수 있습니다.

1.  **Repository Clone**:
    ```bash
    git clone https://github.com/your-repo/withsejong.git
    ```
2.  **Open in Android Studio**:
    Android Studio를 실행하고 `Open`을 선택하여 프로젝트 폴더를 엽니다.
3.  **Sync Gradle**:
    프로젝트가 열리면 Gradle Sync가 자동으로 진행됩니다. 완료될 때까지 기다립니다.
4.  **Run App**:
    에뮬레이터 또는 실제 기기를 연결하고 `Run` 버튼(▶️)을 클릭합니다.

---

## 📱 사용 가이드 (Usage Guide)

### 회원가입 및 로그인
1.  앱 실행 후 `회원가입` 버튼을 누릅니다.
2.  학번과 비밀번호를 입력하고 세종대 인증을 진행합니다.
3.  가입 완료 후 로그인을 진행합니다.

### 게시글 작성하기
1.  홈 화면 또는 게시판 탭에서 `글쓰기` 버튼을 누릅니다.
2.  제목, 내용, 태그를 입력하고 필요 시 사진을 첨부합니다.
3.  `등록` 버튼을 눌러 게시글을 업로드합니다.

### 채팅하기
1.  게시글 상세 화면에서 `채팅하기` 버튼을 누릅니다.
2.  상대방과의 채팅방이 생성되며 대화를 시작할 수 있습니다.

