# 10.31 Iteration 1 Demonstration

## 조직 구성

### 셀

#### Iter 1 셀 A

**_이상헌_**, 박정양, 윤장한, 홍세아

- 협력적인 토론을 통해 이해를 공유하고, 이를 바탕으로 진행됩니다.
- UC15 개발을 맡습니다.
- 정기 미팅은 월요일 저녁, 금요일 저녁입니다.

#### Iter 1 셀 B

**_윤정현_**, 전희재, 박민지

- 분석적인 셀장님의 주도로 각자의 아이디어를 십분 발휘하여 진행됩니다.
- UC19 개발을 맡습니다.
- 정기 미팅은 월요일 저녁, 금요일 저녁입니다.

### 파트

#### 백엔드 파트

박정양, 윤정현, 이상헌, 전희재, 홍세아

- 자료구조 및 API 설계와 개발, 테스트를 담당합니다.
- 제시된 Java 및 Spring Boot를 이용합니다.
- 파트장은 없는 대신, 셀장을 백엔드 파트에서 돌아가며 맡습니다.
- 월요일 또는 금요일에 세미나 미팅을 가집니다.

#### 프론트엔드 파트

**_박민지_**, 윤장한

- 화면 및 UI를 그리고 백엔드와 연결합니다.
- 제시된 AngularJS를 메인으로 사용하며, 필요에 따라 리팩토링합니다.
- 소수 정예이며, 파트장을 돌아가며 맡습니다.
- 워낙 귀한 분들이고 고생하시는 만큼 별도의 파트 미팅은 없습니다.

#### 인프라 파트

**_성현준_**, 김지안

- 테스트 커버리지 측정, 빌드 및 배포 자동화, integration을 구성합니다.
- Docker, GitLab Pipelines, AWS 등을 사용합니다.
- 소수 정예이며, 파트장을 돌아가며 맡습니다.
- 비정기적으로 세미나 미팅을 가집니다.

## UC 플래닝

[UC 플래닝](./presentation/1031_first_iteration/uc-planning.md)

## 신규 UC 제안

[신규 UC 제안](./presentation/1031_first_iteration/new-uc.md)

## 리팩토링

취미로 원본 코드를 리팩토링 중입니다.
도움이 되지는 않더라도, 코드를 공개할 계획입니다.

- [x] restore application.yml
- [x] Local DB with Docker Compose
- [x] Fix package name edu.ncsu.csc.iTrust2 -> edu.ncsu.csc.itrust2
- [x] Maven -> Gradle
- [x] JDK 17
- [x] Spring Boot 2.7
- [x] automated code formatter
- [x] circular dependency is detected
- [x] field injection by @Autowired -> constructor injection (why unit test is using @SpringBootTest?)
- [x] Lombok
- [ ] OpenApi (Swagger)
- [ ] Gson -> embedded Jackson
- [ ] why does each service have only one repository? (what is the abstract class Service)
- [ ] controller should not have business logic
- [ ] remove abstract Controller
- [ ] rename UPERCase to PascalCase (serialization 시 이슈 발생 가능)
- [ ] MapStruct
- [ ] refine test code
- [ ] N+1 problem

## Cell A - UC15

[Cell A](./presentation/1031_first_iteration/cell-a.md)

## Cell B - UC19

[Cell B](./presentation/1031_first_iteration/cell-b.md)

## Infra

[Infra Iteration 1](./presentation/1031_first_iteration/cicd.md)

## 의의

- 협업 룰을 정하고, 이를 중심으로 개발 환경을 설정했습니다.
- 프로젝트 구조 및 UC들을 대략적으로 파악하였습니다.
- Git을 활용한 협업 방식의 기초를 익혔습니다.
- Spring Boot 개발의 기초를 익히고, 실제로 API를 개발했습니다.
- 향후 플래닝을 위한 스토리 포인트 지표를 확보중입니다.

## Iteration 2 초반 계획

Iteration 1 종료 미팅

- 11/2~3 사이에 진행합니다.
- 회고를 진행합니다.
- 향후 리드를 정합니다. 증분 1 개발이 마무리될 때까지 보류할 수 있습니다.

개발, Contd.

- 예정된 feature 개발을 진행합니다.
- 단위 테스트 커버리지 80%를 달성합니다.
- 프론트엔드-백엔드를 통합합니다.
- UC15, UC19 개발을 마무리합니다.

피드백

- 통합 테스트를 진행합니다.
- Acceptance test를 통해 UC 및 요구사항을 충족하는지 확인합니다.

플래닝 및 셀 배정

- UC16~UC18 중 하나를 선정합니다.
- 안과 시리즈 3개를 개발할 셀과 다른 두 UC를 개발할 셀로 나눕니다.
- 이때까지의 지표를 바탕으로 향후 일정에 대한 상세 계획을 정합니다.
- 필요 시 셀 및 역할을 재구성합니다. 각자의 관심사, 스타일, 역량 등을 별도로 기록중입니다.

Iteration 2, 증분 2도 즐겁게 개발해봅시다~
