# Anime Social App

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
Review, Recommend, and Curate Lists of Animes in a social way with friends

### App Evaluation
[Evaluation of your app across the following attributes]
- **Category:** Social/Entertainment
- **Mobile:** 
    * Ability to create and share lists easily with friends
    * Post reviews quickly
    * Infinite Scrolling
- **Story:**
    * For anime fans who enjoy reviewing anime and making lists
    * Anime fans who want a more social experience
- **Market:**
    * The market is a growing one as the anime market is something that is failry new to the mainstream. Especially in western audiences
- **Habit:**
    * Somewhat habit forming because reviewing to those who like to review is fun. So giving an app that allows this to be done rather quickly and easily will form habits.
- **Scope:**
    * This app will use an api to get anime and a database to store users, reviews, and lists. Since we are only building the database from scratch it is a doable project in the given time.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* Sign up/Log in to the app
* Logout from app
* Create/Edit lists of anime by searching and adding from Jikan API
* Post a review of an anime on the home timeline
* View anime reviews and lists
* User can share their lists or reviews as a link outside of the app
* Reccomended anime based on what you have in your lists

**Optional Nice-to-have Stories**
* Follow other users and be followed
* View anime reviews and lists of people they are following on home timeline
* See details and information of specific anime

### 2. Screen Archetypes

* Login Screen
   * Sign up/Log in to the app
* Sign Up Screen
   * Sign up/Log in to the app
* Home Timeline Screen
   * View anime reviews and lists
* Profile Screen 
   * Follow other users and be followed
   * Create lists of anime by searching and adding from Jikan API
   * Logout from app
* Post Screen
   * Create reviews of anime by searching and adding from Jikan API
* Review Detail Screen
   *  View anime reviews and lists
* List Detail Screen
   *  View anime reviews and lists
* Anime Detail Screen
   * See details and information of specific anime
* Recommendation Screen
   * Reccomended anime based on what you have in your lists

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Home Timeline Screen
* Post Screen
* Recommendation Screen
* Profile Screen

**Flow Navigation** (Screen to Screen)

* Home Timeline Screen
   * List Detail Screen
       * Anime Detail Screen
   * Review Detail Screen
       * Anime Detail Screen
* Recommendation Screen
   * Anime Detail Screen

## Wireframes
[Add picture of your hand sketched wireframes in this section]
<img src="YOUR_WIREFRAME_IMAGE_URL" width=600>

### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema 
[This section will be completed in Unit 9]
### Models
[Add table of models]
### Networking
- [Add list of network requests by screen ]
- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]
