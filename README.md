# DeBoard

--- 
## 가장 기본적인 게시판 기능을 구현 하였습니다.
## 주요 기능
- 게시글 생성, 수정 및 삭제
- 게시글 페이징
- 좋아요 기능
- 댓글 및 대댓글 작성 및 삭제
- 회원기능
- Jwt 기반 인증 구현 (블랙리스트 방식)
- 웹소켓을 이용한 채팅 기능 구현

## 사용 기술 스택  
- **프론트엔드** : React.js, Daisy UI, Tailwind css
- **백엔드** : Spring Boot, Spring Security, JPA, WebSocket
- **DB** : MySQL
- **배포** : AWS EC2, AWS RDS, AWS Elastic Beanstalk
## 도메인 및 API 명세  
# API 명세
### AUTH
 **HTTP 메서드** | **엔드포인트**                | **역할**           |
|--------------|--------------------------|------------------|
| POST         | /api/auth/signup         | 회원가입             |
| GET          | /api/auth/check-email    | 이메일 중복 검사        |
| GET          | /api/auth/check-nickname | 닉네임 중복 검사        |
| POST         | /api/auth/check-auth     | 현재 로그인 상태 확인     |
| POST         | /api/auth/login          | 로그인              |
| POST         | /api/auth/refresh/logout | 로그아웃             |
| POST         | /api/auth/refresh | 리프레시 토큰 검사 및 재발급 |

### POST
 **HTTP 메서드** | **엔드포인트**           | **역할**     |
|--------------|---------------------|------------|
| GET          | /api/posts/{id}     | 게시글 상세 조회  |
| GET          | /api/posts          | 게시글 페이징 조회 |
| POST         | /api/posts          | 게시글 저장     |
| PUT          | /api/posts/{postId} | 게시판 수정     |
| DELETE       | /api/posts/{postId} | 게시판 삭제     |

### LIKE
 **HTTP 메서드** | **엔드포인트**                  | **역할**    |
|--------------|----------------------------|-----------|
| GET          | /api/posts/{postId}/likes  | 좋아요 여부 조회 |
| POST         | /api/posts/{postId}/likes  | 좋아요       |

### COMMENT
 **HTTP 메서드** | **엔드포인트**                  | **역할**   |
|--------------|----------------------------|----------|
| GET          | /api/posts/{postId}/comments  | 댓글 목록 조회 |
| POST         | /api/posts/{postId}/comments  | 댓글 작성    |
| PUT          | /api/posts/{postId}/comments  | 댓글 수정    |
| DELETE       | /api/posts/{postId}/comments  | 댓글 삭제    |

### CHAT
 **HTTP 메서드** | **엔드포인트**                     | **역할**       |
|--------------|-------------------------------|--------------|
| GET          | /api/chatroom                 | 내 채팅방 목록 조회  |
| POST         | /api/chatroom                 | 채팅방 생성       |
| GET          | /api/chatmessage/{chatRoomId} | 채팅방 채팅 내역 조회 |
| WEBSOCKET    | /pub/{chatRoomId}             | 채팅방으로 채팅 발행  |

## 프로젝트 개발 일정  
02.27 ~ 03.31
## 아키텍쳐  
## ERD  
https://www.erdcloud.com/d/qkpus97pCBTs9jJoB