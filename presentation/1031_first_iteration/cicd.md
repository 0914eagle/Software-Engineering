# CI/CD
## CI/CD 개념
Continuous Integration, Continuous Delivery의 약자.
![cicd_elements](../../resources/cicd_elements.png)
배포과정(매 이터레이션)마다 plan → code → build → test → release → deploy → operate → monitor을 하게 되는데, 이 과정을 최대한 많이 자동화하는 과정.


CIntegration: Build, Test, Release (개발관점이 더 가깝다. 개발자들이 직접 할 때도 많다)
CDelivery: Release, Deploy, Operate (운영팀에서 쓰인다)


## CI/CD의 목적 및 구체적인 태스크
### CI/CD 핵심: 반복적인 작업을 시스템적으로 자동화하는 것
- 실수의 여지가 줄어듦
- 개발자가 신경쓸 게 줄어서 생산성 오르는 게 체감됨
- 인적자원(개발자의 시간) 관점에서 적은 투자로 많이 아낄 수 있음

## CI 태스크
Test Coverage 달성 여부 → 80% 달성했는지?
- ./gradlew check를 돌렸을때 coverage report를 생성하고, 80%를 넘겼는지 확인.
- UnitTest는 gradlew check을 했을때 JUnit과 Jacoco를 사용하고 있기 때문에 될것.
- 통합 Test는 Selenium + Cucumber을 활용.

## CD 태스크
- CDK에 깃랩 파이프라인 사용하여 최대한 많은 영역 자동화하기
- 배포 옵션 정리, 환경 구성 배포 완료 이후 → CD로 넘어가기

## 시행착오

## 향후계획
