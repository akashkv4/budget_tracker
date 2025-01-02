Budget Tracker App

App Screens : 

MainActivity-> MainActivity have a toggle for changing the theme to dark mode.Also navigation 
library and safe args used.

Create Or Edit Record-> Add expense record with input name,amount,date,category . Editing options is
also available in this screen.

Record List Screen -> In this screen user can view all expense in a recyclerview. This recyclerview 
is done with pagination for handle large data sets. In this screen user can edit or delete the record,
also undo option provide in a snack bar in case of rollback of deletion. User also present with 
list of categories as chips group  where user can select or unselect category. The list is 
observed by using flow. Create record button is also presented in this screen. The reason FAB is 
not used is ,it might cover the edit button of recyclerview.

Chart Screen-> In chart screen current month's expense is shown in pie chart with category.
Also user can edit monthly budget. After editing monthly budget user can view balance, amount spend,
in pie chart . MPAndroidChart library is used for the implementation of pie chart.

Edit monthly budget screen-> This screen enables the editing of monthly budget for user


Architecture:

* Used Mvvm architecture
* Separation of concerns handled with data,domain,presentation layers
* Hilt is used for dependency injection
* Room is used for database
* Use cases are used to encapsulate specific business logic, ensuring clear separation of concern
* Repository layer written for LocalDataSource , also leave space for RemoteDataSource which 
  can be used in the future

Testing:

*Wrote testing for room database


