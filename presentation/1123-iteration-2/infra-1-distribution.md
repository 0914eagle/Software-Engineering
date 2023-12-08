# 인프라 #1

## EB 배포

- EB environment 생성
  - application과 envieonment 이름 작성
  - Platform - java (이하 설정은 default 그대로)
  - application code
    - sameple application(연습용)
    - upload your code(스프링부트 빌드 후 생긴 _**plain이 안 붙은 jar 파일**_ 업로드)
  - Presets은 single instance(free tier eligible)로 진행하였음
  - service access(role 설정)
    - Use an existing service role

      ![Untitled](../../meeting-logs/resources/1107-infra-images/Untitled.png)

    - 만약, 배포 중 _**해당 service role이 존재하지 않는다는 오류**_가 생길 경우, 로그에서 말하는 role과 같은 이름으로 role을 만들어주면 해결된다.

      ![Untitled](../../meeting-logs/resources/1107-infra-images/Untitled 1.png)

  - Instance settings- subnets은 웬만하면, 두 개 이상 체크하기(한 곳에 문제가 생기면 다른 subnets을 이용할 수 있도록)
  - Enable database는 off해두었다. RDS를 사용하여 스프링부트에서 직접 연결할 것이라고 생각하였기 때문이다.
  - Auto scaling group- 여러 대의 인스턴스(서버)를 띄워 로드밸런서로 요청을 분산시키고 싶다면, Single instance에서 Load balanced로 변경하고, 이하 설정도 원하는 대로 수정한다.
  - Environment properties-SERVER_PORT 5000추가(아래 nginx port 설정 참고)

    ![Untitled](../../meeting-logs/resources/1107-infra-images/Untitled 2.png)

## nginx port 설정

- 만약, 배포 후 _**502 Bad Gateway(nginx)라는 오류**_가 생긴다면?

  ![Untitled](../../meeting-logs/resources/1107-infra-images/Untitled 3.png)

- 오류 원인: ALB(로드 밸런서)는 5000 port를 연다. 즉 Nginx가 바라보는 port number가 5000인데, 이 서버는 8080포트에서 열리기 때문에, 불일치로 생긴 문제이다.
- 해결 방법:  yml에서 코드를 직접 수정해주거나, 환경 변수로 yml의 8080 무시하기, 환경변수로 ALB 뒤에 열리는 port number 바꾸기가 있다.
- 환경변수에 SERVER_PORT 5000을 추가하여 해결한다.

  ![Untitled](../../meeting-logs/resources/1107-infra-images/Untitled 4.png)

## RDS

(※ 연결 마무리는 인수인계 이후 완수됨)

- database 인스턴스 생성
  - choose a database creation method-standard create
  - Engine options-MySQL
  - Templates-Free tier
  - DB instance identifier -이름짓기
  - Master username/Master password- db연결을 위해 필요, 비공개되어야 한다.
  - Public access-db와 편하게 연결하려면 yes
  - Additional configuration-Inital database name-이걸 안 쓰면, RDS가 db instance가 생성될 때 database를 만들지 않는 것 같다.
- 스프링부트 적용
  - build.gradle

      ```java
      implementation 'com.mysql:mysql-connector-j'
      ```

  - application.yml

      ```java
      spring:
        datasource:
          url: jdbc:mysql://(엔드포인트):(포트번호 3306)/(식별자)?serverTimezone=UTC
          driver-class-name: com.mysql.cj.jdbc.Driver
          username: (username)
          password: (password)
      ```

- DBeaver 적용
  - 새 데이터베이스 연결하기 - sql선택 - 로컬호스트 부분에 rds 엔드포인트 입력- username, password 입력.
- Too many connections(커넥션 관리)
  - 현상: DB관리 툴에 연결하거나 DB와 연결된 새로운 서비스를 배포할 때, “too many connections”가 뜨며 RDS를 이용하지 못하게 될 수 있다.(아래 connection이 꽉차있을 것)

    ![Untitled](../../meeting-logs/resources/1107-infra-images/Untitled 5.png)

  - 원인: 각 rds 인스턴스마다 연결할 수 있는 최대 횟수(max_connection)에 한계가 있기 때문에 오류가 발생한 것이다. 이 값은 RDS 메모리(ex. t3.micro)에 따라 달라진다.
  - 해결: AWS Console-RDS-파라미터 그룹으로 가서, max_connection 파라미터 값을 올린 새로운 파라미터 그룹을 생성하고, 해당 RDS 인스턴스의 파라미터 그룹을 새로 생성된 그룹으로 변경하고, 데이터베이스 재부팅을 진행한다.

    ![Untitled](../../meeting-logs/resources/1107-infra-images/Untitled 6.png)

    ![Untitled](../../meeting-logs/resources/1107-infra-images/Untitled 7.png)

  - 혹은 이미 연결되어 있는 connection을 일부 없앨 수도 있다.
  - (파라미터를 수정해줄 때, wait_timeout도 함께 조정해줄 수 있다. 클라이언트가 실제로 사용하지 않더라도, 이 값 동안 DB는 계속 커넥션을 붙잡고 있다. → 이는 비효율적이므로 줄여주는 것도 좋다.)
- Security Group(Inbound, outbound)
  - RDS 인스턴스와 연결이 불가능할 경우(cmd에서 ping을 보내는 것도 실패한다면), security group 문제일 수 있다. (←보안 그룹 설정이 현위치에서의 연결을 허용하지 않는 것)
  - AWS 콘솔 - 해당 database- Security-VPC security groups-Inbound rules-Edit inbound rules→아래와 같은 rule을 추가해준다.(source는 anywhereIPv4를 선택했는데, 0.0.0.0/0이라 custom도 무관)

    ![Untitled](../../meeting-logs/resources/1107-infra-images/Untitled 8.png)
- Restoring at specific time
  - RDS database 인스턴스는 특정 시점마다 스스로 백업한 snapshot을 만든다.
  - 혹은 원하는 시점에 사용자가 직접 snapshot을 만들어둘 수 있다.
  - 만약 과거 특정 시점의 database 내용물을 확인하고 싶다면, 해당 database instance나, 그 database로 부터 생성된 snapshot를 통해 ‘Restore to point in time’을 할 수 있다.

  ![Untitled](../../meeting-logs/resources/1107-infra-images/Untitled 9.png)
  - 그러면, 해당 시점의 내용물을 그대로 가진 db 인스턴스가 생성된다. → 이것을 연결하여 내용물을 직접 확인 및 관리 툴을 이용하여 원하는 db로 내용물을 옮길 수 있다.
  - 주의: 따로 원하는 시점의 snapshot을 남겨두지 않는 이상, restore할 수 있는 시점을 완전히 자유롭게 설정할 수는 없으므로, 데이터 백업의 주된 방법으로 사용하는 것으로는 옳지 않다고 생각한다. (하루 이틀 이전 db 정도는 웬만하면 원하는 대로 백업되는데, 좀 더 예전 내용물은 더이상 저장되어 있지 않아 확인하기 어려웠다.)
  - 주의: 이 방식으로 생성된 db 인스턴스도 똑같이 비용이 나가므로, 더이상 필요 없다면 바로 삭제해야 한다.

## IAM

- 한 계정(루트 계정)의 AWS 리소스를 다른 사람들도 공유할 수 있도록 IAM 계정을 만들 수 있다.
- Root 계정 MFA 설정(현재 적용되어 있다.)
  - AWS 콘솔- IAM-루트 계정 MFA 추가 - 가상 인증 앱 다운 등의 방식 중 하나를 골라 진행.

    ![Untitled](../../meeting-logs/resources/1107-infra-images/Untitled 10.png)

    [IAM - 다중 인증](https://aws.amazon.com/ko/iam/features/mfa/?audit=2019q1)

    [[AWS] AWS MFA 설정](https://doing7.tistory.com/29)

- IAM 계정 생성+(모든) 권한 추가(현재 두 개의 IAM 계정을 공유 중이다.)
  - IAM-Users-Create User
  - User name 작성
  - Provide user access to the AWS Management Console(optional이지만 선택함)-Usertype은 ‘I want to create an IAM user’로 선택했다.
    - Console password-직접 지어도 되고, 자동생성해도 된다. 나중에 IAM 계정사용자가 처음 접속할 때 비밀번호를 바꿀 수 있다.
      - Set permissions- 아래 옵션을 선택하고, permissions policies에서 해당 IAM 계정 사용자에게 주고 싶은 Root 계정에 관한 권한을 선택한다.
        - Administrator Access는 Root의 모든 리소스에 관한 권한을 IAM에게 허가한다.
        - IAMUserChangePassword는 IAM 계정 사용자가 비밀번호를 바꿀 수 있도록 한다.(아래 단계에서 따로 선택하지 않아도 자동으로 추가됨)

        ![Untitled](../../meeting-logs/resources/1107-infra-images/Untitled 11.png)

        ![Untitled](../../meeting-logs/resources/1107-infra-images/Untitled 12.png)

        ![Untitled](../../meeting-logs/resources/1107-infra-images/Untitled 13.png)

  - 이후 생성된 csv 파일의 링크에 접속해서 IAM용 id와 password를 입력하면 사용할 수 있다.

## Budget

- AWS 요금 관리를 위해, 사용자가 직접 주기적으로 Billing 내역을 확인하는 것 외에도, 예산안 한도를 미리 정해두는 Budgets 기능을 사용할 수 있다.
- AWS Billing-Bugets-Overview-Create budget
  - Templates-Monthly cost budget으로 선택했다.
  - Budget name, budgetd amount, Email recipients
- 만약 미리 정해준 예산 한도를 넘는다면 budget을 만들 때 기입한 이메일들로, 알림이 온다.(85%등으로 알림 시기를 조절할 수 있다.)
- 현재 팀 프로젝트용 계정에 월 50$를 예산안으로 생성해두었다. 70%, 100%에 도달할 경우, 인프라 팀원의 (gitlab에 올린) 메일로 알림이 전송된다.

  ![Untitled](../../meeting-logs/resources/1107-infra-images/Untitled 15.png)
