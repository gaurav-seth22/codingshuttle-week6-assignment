## Codingshuttle-week6-assignment

### Key Highlights
1.	/logout Route with User Session Management
  o	This feature provides a logout mechanism that:
    •	Deletes the user's current session.
    •	Removes the refresh token (refreshToken) stored in the user's cookies.
    •	Ensures proper cleanup of user data to prevent unauthorized access after logout.
2.	Refresh Token Rotation
  o	Implements a secure refresh token mechanism by:
   •	Deleting the user’s existing refresh session as soon as a new /refresh or /login request is processed.
   •	Generating a fresh pair of Access Token (AT) and Refresh Token (RT) upon each request.
   •	Storing the new tokens in the user’s session to maintain security and prevent token reuse attacks.
3.	Dynamic Session Limit per User
  o	Introduces dynamic session management by:
   •	Associating a sessionLimitCount with each user in the system.
   •	Allowing each user to have a customizable number of active sessions, based on the configured limit.
   •	Managing session creation and termination to ensure the limit is adhered to.

   ### API Endpoints testing

   #### Login

   ![image](https://github.com/user-attachments/assets/36a439c8-d97b-404d-815f-7bc4ff367395)

   #### AT testing

   ![image](https://github.com/user-attachments/assets/6c084f52-70af-4aa8-bad8-254dafa8f80d)

   #### RT

   ![image](https://github.com/user-attachments/assets/0ead9626-8db2-42b6-b3c2-82fa88d23168)

   ![image](https://github.com/user-attachments/assets/ce2cfd43-346d-43e3-9141-ae5b754c55dd)

   #### Rotated AT and RT

   ![image](https://github.com/user-attachments/assets/d648d40b-6236-4181-9866-8bd1459052ab)

   #### With old RT(Unauthorised)

   ![image](https://github.com/user-attachments/assets/d39721a9-e11d-4a07-8497-f3ef5a3ad9e2)

   ### Logout

   ![image](https://github.com/user-attachments/assets/9e1d4ad8-9b00-4018-b4db-d9fcb20b73c2)

   ![image](https://github.com/user-attachments/assets/83eb1f26-40c4-4707-adc5-721eb695ea0f)

   ### Session Limit test

   #### After two login first session got evicted

   ![image](https://github.com/user-attachments/assets/d03d3af8-e921-4661-975d-329c6294d988)

   ![image](https://github.com/user-attachments/assets/2f33c58f-4f62-4d75-9004-668f05199bcb)

   ### Subscription Service test

   ![image](https://github.com/user-attachments/assets/ea49b78b-733b-461f-aa1c-cb2f2b0af893)

   ![image](https://github.com/user-attachments/assets/0b947faf-0501-4809-8bae-0cf5fdf693d4)

   #### Plan access check

   ![image](https://github.com/user-attachments/assets/82bc0719-57b0-4b2e-a5ab-d612712e2a6e)

   #### Forbidden to non subscribed plan

   ![image](https://github.com/user-attachments/assets/377fe6c4-d651-4c7f-97c8-edf8f10c57d3)

   #### Plan Upgrade

   ![image](https://github.com/user-attachments/assets/fbbe9b19-ada9-4a40-8818-ebd0a8149613)

   #### Plan access check

   ![image](https://github.com/user-attachments/assets/dbce665c-26a4-4a22-a9ae-a2d67b682cb6)



   

   
   


   








