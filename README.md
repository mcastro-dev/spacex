# SpaceX Android App
(Created for Mindera's app challenge, in December 2021)

![Light](https://lh3.googleusercontent.com/wNBgGPYGbVptEOrtUj0v7CfXTzEda1lGkt97D4IwL5EIx0hlNHNSJR2Re_NnbcJhYvIJJc-RXtX-oFaqROnvuXwW8Yiw1N7h_HcBRwEAVk05eeB3Gv0R_kQcghOnv8LceF6V1VGTBQ=w240)
![Dark](https://lh3.googleusercontent.com/D631yRgV_0lRt3q8M3mKKKtsnVImcxFda295Q5aIL3mXR3ruAFyOVFJ3UQZWvMDVO-6R_fuuHIgGCd37dnEOxAmALZrvKLHkeN-CnAfGbNScpGbBNkp4sGQHU2BhnwGp1ubY4uZW1g=w240)

**API**
- REST: https://api.spacexdata.com/
- GraphQL (non-official): https://api.spacex.land/graphql/


# Requirements

- [x] Use the SpaceX API;
- [x] Company section, populated by the API's data, displaying:
	- [x] "{company name} was founded by {founder name} in {year}. It has now {employees} employees, {launch sites} launch sites, and is valued at USD {valuation}";
- [x] Rocket Launches section, populated by the API's data, each item displaying:
	- [x] Mission name;
	- [x] Launch's date and time;
	- [x] Rocket name and type;
	- [x] Days since/from launch;
	- [x] Mission patch image;
	- [x] Successful launch or not;
- [x] Filter rocket launches by:
	- [x] Years
	- [x] Launch status
- [x] Sort rocket launches (ascending and descending)


# About
- Language: Kotlin
- Orientation: Portrait (landscape is not disabled, but not ideal)
- Unit tests: 53 (all passing)
- Light and Dark themes
- Tested on Android API 30 emulator
- Dependencies:
	- [Mockito](https://site.mockito.org/)
	- [Room](https://developer.android.com/jetpack/androidx/releases/room)
	- [RxKotlin](https://github.com/ReactiveX/RxKotlin)
	- [Retrofit](https://square.github.io/retrofit/)
	- [Picasso](https://square.github.io/picasso/)
	- [Hilt](https://dagger.dev/hilt/)
	- [Apollo](https://www.apollographql.com/docs/kotlin/)


The app contains all the requested features.
<br/>A single page, where the user can see 2 clearly defined sections: Company and Launches.
<br/>Users may filter rocket launches by minimum launch year and launch success, as well as order results by launch date in ascending or descending order.
<br/>Touching a rocket launch item from the list opens up a dialog where the user may use links to read its article, Wikipedia, or watch its video.

![Usage](https://lh3.googleusercontent.com/9eBcqxrbG3RKglu0eSpjaHofZPtrfU1oOHaIazvjiRbmtFL5mRxWQ3G9bkYSMtSWm297_ZcLjI4AR4bqF3yd2Wf5JzjA9CUDjmOHtHvi8pGdxNy7bkNnho1ZWoSObivPHKChe1M4WA=w600)

On the first half of the development I used TDD (Test-Driven Development), but as the days went by, I started worrying about not having enough time to implement everything (and felt like you guys could already have a sense of how I implement unit tests). That's when I dropped TDD and just developed the rest of the features, leaving the missing unit tests to whatever remaining time I would have.

# App Flow

> I tried my best to follow the Clean Architecture and DDD (Domain-Driven Design) teachings. Below are simplified sequence diagrams of the user interactions within the app.
> <br/> PS: Please, don't expect too much of it; I don't know UML  rules or "best practices", but I still risk myself using it from time to time because I think it's helpful.


- **Home page: GetInitialDataEvent**
  - 
Occurs as soon as the Home page is loaded, requiring no action from the user.

![enter image description here](https://lh3.googleusercontent.com/Arie7xhwgWITn8VNQUSuwcI77WhdSBwKQ_uNvNzuj4-nDgxAEDQfs1vDmG42ayAoGsIymnmYkp-DxGAvYYu7cTN6CgqJXL-b0_t0IAiygWgV0XwUI8V_gw8_ec4wqH0lNduz2UAiNA=w1640)

![enter image description here](https://lh3.googleusercontent.com/9QvQQlPnSwkTxkK4RE8IQPd_0AUeJbUtXS7_OmwG9z0Ms1SdxEO9bqfIFXTQ82EFqjaFkt-z0DPenkmJDiQ8jsFxWME-jzuDJhnfXq91FffeAhUMbrJOCAdBX31SLFB282lcAlMfQQ=w1640)


- **Home page: RefreshEvent**
  - 
Occurs when the user pulls down the list, triggering the RecyclerView's "swipe to refresh" feature.

![enter image description here](https://lh3.googleusercontent.com/XDbZrqWlCbC5RfIMuyvYUUDzpIw8iVVh2pTRQ_aPwUDrCyCaGQVOYqo0Kpk5oDP0QMH7tXVW4T1ISt51NegIyY79VTaZ1zKpfi4uv5uAhFhu8vBvWkIa8r5VAj92B4DQe_C71UhhFw=w1640)


- **Home page: ApplyFiltersEvent**
  - 
Pre-required action: open up the filter's dialog by touching the top-right action bar button.
Occurs when the user touches the "APPLY" button.

![enter image description here](https://lh3.googleusercontent.com/Rxm5_EPoQJ3GBI4xWW9XUITtj3Wmvr4DaoDWxmeQcRu1GhI22wweXXoQk5iV8RRBqJOOmU7w-EcZ3u1BNPGzbFH83UXJvBenH_Kd-9ncKDPi22j5yZBk2Gxt6idF0cYUN4NB-HhWLw=w1640)


# REST vs GraphQL
When I started developing the filter feature, I had to do some research about how to query the launches data.
<br />Besides the official [REST API](https://api.spacexdata.com/) "/v4/launches/query" endpoint, I found a SpaceX [GraphQL API](https://api.spacex.land/graphql/), that I later found out to be a non-official API (should've read their readme file more carefully  &#128517;).

As I've never worked with GraphQL, I figured it would be a great opportunity to learn a little bit.
<br />So, after implementing the filter using the REST API, I implemented it using the GraphQL API as well. But unfortunately, this non-official API does not provide a way to filter rocket launches by "launch years greater than", and therefore had to switch back to the REST implementation.
<br />However, since it took me a lot of time and effort to implement, I decided to keep the GraphQL data source in the project.

**If you wish to test the GraphQL implementation, you just have to replace one by the other in the dependency injection file.**

There's another important reason not to use the GraphQL API though...  
The following requests return no results (even though the REST API returns 1+):
- Requests filtering by "launch_success", for both values "true" and "false"
- Requests filtering by "launch_year", only when its value is year "2021"

You may verify it by running the following queries on https://api.spacex.land/graphql/:

    {
      launches(
        limit: 3,
        offset: 0,
        find: {
          launch_year: "2021"
        },
        order: "asc",
        sort: "launch_date_utc"
      ) {
        id
        mission_name
        launch_year
        launch_success
      }
    }

and

    {
      launches(
        limit: 3,
        offset: 0,
        find: {
          launch_year: "2020",
          launch_success: "true"
        },
        order: "asc",
        sort: "launch_date_utc"
      ) {
        id
        mission_name
        launch_year
        launch_success
      }
    }
![launch_success: "true"](https://lh3.googleusercontent.com/mz5uLvHUsu-vZtNT-jm9vCjUgehe_P055k8QtD9NzMk_xaCUowarYiewoK6bFh2YKlxRwQJy3Miq97xZKVU1iLrz7OtMOzjgEE_zP5DtBIE8Keub8UmKGav1or2EzN8DOS2Yrtei7A=w400)
![enter image description here](https://lh3.googleusercontent.com/PaQMLdnZZDe2DwT8lQSCcHT0kAStL0v3MVLh2aDKzOrZDFbxQJSXAPiADfOg7YDDGVne932l-c0DHZNexh7zJrdAY9w-x6hhCJ6T51ODBc10ZblzH4lAj05dEWed6s3Dq_rMIvHQGw=w400)

# Main Packages

> I usually create packages with readability in mind.
> <br />For example: /home/rocketlaunch/filter
> <br />By doing that, let's say I have to implement something on the **filter** of the **rocket launches** list of the **home** page. It's right there on the package name.
> <br />Inspired by &#128561; architecture

- **com/mindera/rocketscience/home**
  - 
	- **/itself**
	  <br />Everything related to (guess what &#128517;) *home itself*.
	  This is the presentation layer of the "Home" component.
	  "itself" is a naming convention I use when I want to highlight an intersection between one context and others. In this case, the intersection between Home/Company and Home/Rocket Launches. See image below.

	- **/company**
	  <br />Home intersection with the Company context.

	- **/rocketlaunch**
	  <br />Home intersection with the Rocket Launch context.

	- **/rocketlaunch/filter**
	  <br />Home's rocket launch list filter feature. Has its own ViewModel, used by its *BottomSheetDialogFragment*.

  ![Contexts](https://lh3.googleusercontent.com/6evl2u8OFwsmbEMC2Xs_x5kxLqlKKOvochUN2zbLv-AW3LKpp5E-UCNUyWj783QlmN4ZJIiL6pZk5t8WmD5qLmM7Y7D9D-ua1LQd7bBnF48oucIXcLz67chGyg5fIfTmLrnG7P5Yxw=w300)

- **com/mindera/rocketscience/company**
  - 
	- **/data/local**
		- /sharedprefs
	- **/data/remote**
	- **/domain**
		- /model
		- /usecase

- **com/mindera/rocketscience/rocketlaunch**
  - 
	- **/data/local**
		- /room
		- /sharedprefs
	- **/data/remote**
		- /spacex/graphql
		- /spacex/rest
	- **/domain**
		- /model
		- /usecase


# To-do

- Unit test the data sources;
- UI tests;
- Segregate the dependency injection modules. They are contained in a single file, but should be distributed across the appropriate packages;
- Create custom view in order to not repeat the same set of XML tags on every line of the rocket launch list item;
- Show loading indicator at bottom of the list as well, when paginating.
- Landscape mode.

# References
https://github.com/r-spacex/SpaceX-API/blob/master/docs/queries.md
https://developer.android.com/kotlin/coroutines
https://github.com/ReactiveX/RxJava
https://developer.android.com/jetpack/androidx/releases/room
https://developer.android.com/training/dependency-injection/hilt-android
https://material.io/components/buttons/android#theming-buttons
https://www.apollographql.com/docs/kotlin/
https://graphql.org/learn/
https://site.mockito.org/
https://square.github.io/retrofit/
https://stackedit.io/


# Developer
Matheus de Oliveira Castro (matheuscastro.dev {at} gmail {dot} com)
