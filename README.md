# AndroidTest
features and dependencies used
1. Authentication (user registration and login feature) implemented with SQLite database with android Room persistence library.
  -insert user details
  -insert path/uri of profile pic chosen from gallery
  -store image to internal storage with path in database
  
 2. Notification feature (when user registers new account, logs in or l0gs out notification is shown)
 
 3. Session management for logged in users using shared preference and storing user details.
 
 4. Profile Screen (Show details of logged in user from database along with options to update all the details even profile pic and password.
    -user session logged in feature
 
 5. User Screen (display list of all local users registered with the app along with their details and profile pic)
    -Recycler View for list display
    
 6. GitHub Users (fetch list of github users consuming github user api and display the list along with name and profile pic)
    -Retrofit REST api dependency
    -Piccasso and okhttp downloader dependencies to fetch and display images
