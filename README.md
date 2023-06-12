# WeatherApp

* **Project Objective** 
  * This application displays the current weather forecast for your device current location, and display location and all relevant information.
  * This application allows users to input their city and displays the current weather conditions for that city.
 
 
 * **Features**
    * User Interface (UI)
      * A clean and intuitive UI that includes an input field for city names and an area to display the weather information.
    * User Input
      * Users can enter their city name and see the weather details. 
    * Location Access
      * Displays the current weather forecast for the device's current location.
    * API interaction
      * It uses openweathermap Weather API to fetch the current weather details of the city provided by the user.  
    * Display Data
      * After receiving data from the API, It display the weather information on the screen. This information is comprehensive and including temperature, humidity, wind speed, weather info as well as icon of weather. 
    * Conditional Rendering
      * It displays a loading indicator to the user while the network request is being processed.
    * Data Persistence
      * It saves the last searched city name in the local storage of the device and display its weather information when the app is relaunched using SharedPreferances.
      * Saves the response data for offline use and displays it on application startup or in case of no internet connection.
      
## Installation

1. Clone this repository to your local machine.
2. Open the project in your preferred development environment.
3. Make sure you have the required dependencies installed. (List the dependencies and versions if applicable)
4. Build and run the application on your device or emulator.

## Usage

1. On the main screen, you will see an input field where you can enter the name of the city for which you want to see the weather information.
2. After entering the city name, press the enter key or tap the search button.
3. While the application is making the network request to fetch the weather data, a loading indicator will be displayed.
4. Once the data is received, the weather information for the specified city will be displayed on the screen, including temperature, humidity, wind speed, and weather conditions.
5. The application will save the last searched city name in the local storage of the device.
6. When the app is relaunched, it will display the weather information for the last searched city.
7. To search for a new city, simply enter the name in the input field and follow the same steps as mentioned above.

## Dependencies

This application uses the following dependencies:

- [OpenWeatherMap API](http://openweathermap.org/API) : Provides weather data for the application.
- Volley library: Used for making HTTP requests to the OpenWeatherMap API.

Make sure to include these dependencies in your project before running the application.

## Contributing
  * Contributions are welcome! If you find any issues or have suggestions for improvement, please open an issue or submit a pull request.   
  


