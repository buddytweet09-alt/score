# Football Live Score Android App

A comprehensive football live score Android application built with Java, XML, and Material Design 3. The app provides real-time match data, detailed match information, and a premium user experience.

## Features

### 🏠 Home Screen
- **Live Matches**: Real-time scores and match status
- **Upcoming Matches**: Future fixtures with date selection
- **Finished Matches**: Completed match results
- **Favorites Section**: Quick access to bookmarked matches
- **Date Picker**: Navigate through different match days
- **Filter Tabs**: Live, Upcoming, and Finished match filters

### ⚽ Match Details
- **Match Header**: Team logos, scores, competition info
- **Match Info**: Venue, referee, date/time details
- **Lineups**: Starting XI and substitutes for both teams
- **Statistics**: Possession, shots, cards, corners, fouls
- **Match Events**: Goals, cards, substitutions with timeline

### ⭐ Favorites System
- **Bookmark Matches**: Save favorite matches locally
- **Priority Display**: Favorites shown first in match lists
- **Persistent Storage**: Favorites saved using SharedPreferences

### ⚙️ Settings
- **Dark/Light Theme**: Toggle between themes
- **Notifications**: Enable/disable match notifications
- **App Info**: Version details and about section
- **Social Media**: Links to Twitter, Facebook, Instagram
- **Cache Management**: Clear app cache
- **Share App**: Share with friends

## Technical Stack

### Architecture
- **Language**: Java
- **UI**: XML with Material Design 3
- **Build System**: Gradle
- **Architecture Pattern**: MVP (Model-View-Presenter)

### Libraries & Dependencies
- **Retrofit 2.9.0**: REST API communication
- **Gson**: JSON parsing
- **Glide 4.16.0**: Image loading and caching
- **Material Components 1.11.0**: Material Design UI components
- **Room Database**: Local data storage (optional)
- **SwipeRefreshLayout**: Pull-to-refresh functionality
- **ViewPager2**: Tab navigation in match details
- **Work Manager**: Background notifications

### API Integration
- **Provider**: LiveScore Real-Time API (RapidAPI)
- **Base URL**: `https://livescore-real-time.p.rapidapi.com/`
- **Authentication**: API Key in headers
- **Endpoints**:
  - Live matches: `/matches/list-live`
  - Matches by date: `/matches/list-by-date`
  - Match details: `/matches/get-info`
  - Lineups: `/matches/get-lineups`
  - Statistics: `/matches/get-statistics`
  - Match events: `/matches/get-incidents`

## Project Structure

```
app/
├── src/main/java/com/footballscore/liveapp/
│   ├── models/           # Data models (Match, Team, Player, etc.)
│   ├── network/          # API service and client
│   ├── ui/
│   │   ├── activities/   # Main activities
│   │   ├── fragments/    # Fragment classes
│   │   └── adapters/     # RecyclerView adapters
│   └── utils/           # Utility classes (DateUtils, etc.)
├── src/main/res/
│   ├── layout/          # XML layouts
│   ├── drawable/        # Icons and graphics
│   ├── values/          # Colors, strings, themes
│   └── menu/           # Navigation menus
└── build.gradle        # App-level dependencies
```

## Key Features Implementation

### Material Design 3
- **Color System**: Primary, secondary, surface colors with variants
- **Typography**: Consistent text styles and sizing
- **Components**: Cards, buttons, tabs, navigation following MD3 guidelines
- **Elevation**: Proper shadow and elevation system
- **Responsive Design**: Optimized for different screen sizes

### Network Layer
- **Retrofit Configuration**: Custom OkHttp client with logging
- **Error Handling**: Comprehensive error handling for network failures
- **Caching**: Image caching with Glide
- **Headers**: Automatic API key injection

### Data Management
- **SharedPreferences**: User settings and favorites storage
- **JSON Parsing**: Gson for API response parsing
- **Data Models**: Comprehensive model classes for all API entities

### UI/UX Features
- **Shimmer Loading**: Skeleton loading states
- **Pull-to-Refresh**: Swipe refresh functionality
- **Empty States**: Informative empty state screens
- **Error States**: User-friendly error messages
- **Animations**: Smooth transitions and micro-interactions

## Setup Instructions

1. **Clone the Repository**
   ```bash
   git clone <repository-url>
   cd football-live-score-app
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the project directory

3. **API Configuration**
   - The API key is already configured in `ApiClient.java`
   - No additional setup required for API access

4. **Build and Run**
   - Sync project with Gradle files
   - Build the project
   - Run on device or emulator

## Requirements

- **Minimum SDK**: API 24 (Android 7.0)
- **Target SDK**: API 34 (Android 14)
- **Compile SDK**: API 34
- **Java Version**: 1.8
- **Gradle Version**: 8.2.0

## App Permissions

```xml
<!--<uses-permission android:name="android.permission.INTERNET" />-->
<!--<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />-->
<!--<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
```

## Future Enhancements

- **Push Notifications**: Real-time match event notifications
- **League Tables**: Complete league standings
- **Player Profiles**: Detailed player information
- **Match Predictions**: AI-powered match predictions
- **Social Features**: Share matches and results
- **Offline Mode**: Cached data for offline viewing

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support and questions, please open an issue in the repository or contact the development team.