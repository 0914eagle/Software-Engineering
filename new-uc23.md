# UC23 Send Actual Emails

## 23.1 Preconditions

The event requiring an email alert has occurred (UC14)

## 23.2 Main Flow

Any patient can view their email entry [S1]. Change in appointment (both HCP and Ophthalmologist), surgery request [S2], account lockout [S3], or change in Personal Representatives [S4]. And among HCPs and patients can communicate by email [S5]. All emails are tracked within the iTrust2 application and sent to the receiver. All events are logged.

## 23.3 Sub-flows

- **[S1] The patient can view emails that have been sent to them.**
- [S2] The status of an appointment request has been changed by the HCP or Ophthalmologist involved in the request (UC6, S3, **UC20, S3**). Also, the information about an ophthalmology surgery has been changed by the HCP involved in the request (**UC22, S2, S4**). An email informing the patient of their appointment status is sent.
- [S3] A user account has been locked out, either temporarily or permanently, due to too many failed logins. An email informing the user of their lockout is sent. (UC2)
- [S4] The status of a Personal Representative has been changed by the other patient or HCP involved in the request (**UC16, S2, S3, S4**). An email informing of their Personal Representative status is sent to the patient.
- **[S5] Any user can communicate with any other one by email.** If the destination user does not exist, an error message will be shown [E1].

## 23.4 Alternative Flows

- [E1] The destination user does not exist. An error is displayed "Unable to send email because no user found with provided username".

## 23.5 Logging

| Transaction Code | Verbose Description                                          | Primary MID    | Secondary MID | Transaction Type | Patient Viewable |
| ---------------- | ------------------------------------------------------------ | -------------- | ------------- | ---------------- | ---------------- |
| 2301             | Patient views his email entry                                | Logged-in user | None          | View             | Yes              |
| 2302             | AppointmentRequest or SurgeryRequest Email notification sent | Logged-in user | None          | Create           | Yes              |
| 2303             | Account Lockout Email notification sent                      | Logged-in user | None          | Create           | Yes              |
| 2304             | Personal Representative Email notification sent              | Logged-in user | None          | Create           | Yes              |
| 2305             | User sends email to another user                             | Logged-in user | Object user   | Create           | Yes              |

## 23.6 Data Format

```java
//package edu.ncsu.csc.itrust2.models;
public Email(String sender, User receiver, String subject, String messageBody) {
    this.sender = sender;
    this.receiver = receiver;
    this.subject = subject;
    this.messageBody = messageBody;
}
```

- sender: 보내는 이
- receiver: 받는 이(FE에선 User말고 이름으로 입력받기)
- subject: 제목
- messageBody: 내용

## 23.7 Scenario

### **Scenario 1: Email inbox with no email**

Precondition: Bob is logged in as any role. He has received and sent no email.

He enters the email page, and no email is shown.

### **Scenario 2: Email composition**

Precondition: Bob is logged in as any role.

He enters the email page. He clicks the “compose an email” button then a form with title, body, and receiver shows. He fills in the blanks, especially Alice as the receiver, and clicks the “send” button. The email is sent to Alice and shows in his outbox.

### **Scenario 3: Email inbox with emails**

Precondition: Scenario 1, Scenario 2 passed. Alice logged in as any role.

She enters the email page. The email sent by Bob shows in her inbox.

### **Scenario 4: UC2 - User locked out after 3 attempts**

HCP Shelly Vang attempts to authenticate into iTrust2, but uses an incorrect password. A message informs her of her mistake. She tried again 2 more times, after which a message informs her she is locked out of the system. An hour later, she attempts to login with the correct credentials and successfully logs in.

**Additionally, if the user’s email is valid, then the email is actually sent**

### **Scenario 5: UC16 - Add a patient as a PR**

Patient Billy Bob logs into the iTrust2 system, Billy navigates to the PR selection page and adds patient "Jill Bob" as a PR. A confirmation message is shown and Jill Bob appears in Billy's list of PRs

**Then an email that informs the creation of PR is sent to Billy, the represented patient.**

### **Scenario 6: UC16 - Un-assign yourself as a PR**

Precondition

- Jill logs in to the iTrust2 system, Jill navigates to the list of her assigned patients and selects Billy Bob, Jill is taken to a page where she can view the access logs of Billy, Jill can navigate to the medical records, diagnoses, and appointments page

Jill Bob navigates back to the list of assigned patients, Jill selects Billy and un-assigns him as a patient, Jill receives a confirmation message and Billy's name is removed from the list of assigned patients, Jill no longer has access to Billy's medical records

**Then an email that informs the deletion of PR is sent to Billy, the represented patient.**

### **Scenario 7:** UC20 - Ophthalmologist HCP accepts appointment

Preconditions

- Create an ophthalmology appointment in iTrust2 system: Patient authenticates into iTrust2. They navigate to the request appointment page and choose to create a new ophthalmology appointment. They choose an ophthalmologist HCP and enter "03/02/2019" as the date, "12:30 pm" as the time, "Left eye only" as the comments, choose "ophthalmology appointment" as the type, "Jack Doe" as the name. They press the Request Appointment button, and the system displays a message that the appointment was successfully requested.
- View Ophthalmology Appointment Status: Patient authenticates into iTrust2. They navigate to the appointments page to view all. The ophthalmology appointment remains with the status of pending as can be seen by the patient.

An Ophthalmologist HCP authenticates into iTrust2. They navigate to the page to view requested appointments. They see the appointment made by a patient and press the Approve button. A success message is shown indicating the appointment was approved.

**Then the patient receives an email that an ophthalmologist HCP edited the status of the ophthalmology appointment.**

************After that, patient can view the received email in inbox.************

### **Scenario 8: UC22 -** Ophthalmologist HCP edits an ophthalmology surgery office visit

An Ophthalmologist HCP authenticates into iTrust2. They navigate to the edit office visit page and chooses the edit an office visit.

The Ophthalmologist selects a previously created ophthalmology surgery office visit. They enter "50" for the Visual Acuity Results, "-50.0" for Sphere, "-50.0" for Cylinder, "90" for Axis, “cataract surgery” for Surgery Type, and "This is a note" for Notes. They click the Update Office Visit button and the page displays a message saying "Office visit edited successfully".

**Then the patient receives an email that an ophthalmologist HCP edited the information about the ophthalmology surgery.**

## 연관된 UC

- UC14: email
- UC2: lockout
  - 발생 시 해당 유저에게 노티 + 실제로 발송
- UC6: appointment
  - UC14 문서에는 이메일 발송한다는 내용이 있었으나, 실제로 발송하는 코드가 없음
  - **어느 종류든** 정보 변경 시 환자에게 노티 + 실제로 발송
- UC16: Personal Representatives
  - PR 생성 시 대리인, 피대리인에게 노티 + 실제로 발송
  - PR 삭제 시 대리인, 피대리인에게 노티 + 실제로 발송
- UC20: Ophthalmology Appointment Requests
  - **어느 종류든** 정보 변경 시 환자에게 노티 + 실제로 발송
- UC22: Ophthalmology Surgeries
  - 정보 변경 시 환자에게 노티 + 실제로 발송
