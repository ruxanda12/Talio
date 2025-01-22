# Talio - Personal Task List Organizer

This repository contains the **Talio** project, a Personal Task List Organizer developed as part of the **Object-Oriented Programming Project** course at TU Delft. Talio is a distributed client-server application inspired by the "Issues" feature in GitLab, providing a robust platform for managing tasks collaboratively.

## Features

- **Task Management**:
  - Create, edit, delete, and organize tasks within lists.
  - Add labels, descriptions, subtasks, and due dates to tasks.

- **List and Board Management**:
  - Group tasks into customizable lists.
  - Manage multiple boards with unique keys for different projects.

- **Real-Time Collaboration**:
  - Enable multiple users to view and edit boards simultaneously.
  - Auto-synchronize changes across all users.

- **Customizable User Experience**:
  - Personalize board appearance with custom themes, colors, and backgrounds.
  - Implement keyboard shortcuts for efficiency.

## Technologies Used

- **Backend**: Java, Spring Boot
- **Frontend**: JavaFX
- **Data Communication**: REST API, Web Sockets
- **Testing**: JUnit

## Design Patterns Implemented

- **Proxy Pattern**: Secures communication between client and server, ensuring data integrity.
- **Observer Pattern**: Implements real-time updates and synchronization across users.
- **Factory Pattern**: Facilitates dynamic creation of tasks and lists.

## Running the Application

### Prerequisites

- Java 15 or higher
- Gradle

### Steps to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/talio.git
   cd talio
   ```

2. Build the project:
   ```bash
   ./gradlew build
   ```

3. Run the server:
   ```bash
   ./gradlew bootRun
   ```

4. Start the client application by executing the JavaFX application file.

## User Stories

- As a user, I want to create, edit, and delete tasks to manage my work efficiently.
- As a user, I want to organize tasks into lists and boards for better project management.
- As a user, I want real-time synchronization to collaborate seamlessly with others.
- As a user, I want to customize the application to suit my workflow and preferences.

## Contribution

This project was developed collaboratively by a team of six students:
- Maria Ruxanda Tudor
- Yuliia Shkekina
- Teun Klaassens
- Stoyan Markov
- Gerrit Croes
- Diego Becerra Merodio
