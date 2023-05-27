# WeatherApp

* **Project Objective** 
  * This application allows users to input their city and displays the current weather conditions for that city.
 
 * **Features**
    * User Interface (UI)
      * A clean and intuitive UI that includes an input field for city names and an area to display the weather information.
    * User Input
      * Users can enter their city name and see the weather details. 
    * API interaction
      * It uses openweathermap Weather API to fetch the current weather details of the city provided by the user.  
    * Display Data
      * After receiving data from the API, It display the weather information on the screen. This information is comprehensive and including temperature, humidity, wind speed, weather info as well as icon of weather. 
    * Conditional Rendering
      * It displays a loading indicator to the user while the network request is being processed.
    * Data Persistence
      * It saves the last searched city name in the local storage of the device and display its weather information when the app is relaunched using SharedPreferances.
  


