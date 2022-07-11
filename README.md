# W16_R5_Group1_Assignment2

## Table of Contents
- [Background](#Background)
- [Install](#Install)
- [Usage](#Usage)
    - [Setup](#Setup)
    - [Run](#Run)
    - [Test](#Test)
- [Maintainers](#Maintainers)
- [Contributors](#Contributors)
- [License](#License)

## Background
This software is a movie ticket booking system, which can buy movies of different cinemas, different times, different types and different screen sizes. At the same time, the software has a staff mode and manager mode, which is used to facilitate the administrator to manage the number of films, the change of staff and the arrangement of projection halls.

## Install
Installation options are not available for this project. This project is builded on [gradle 6.8.3](https://gradle.org/install/) with [java 11.](https://java.com/en/download/help/download_options.html) Please set up the right system environment.


```
$git clone git@github.sydney.edu.au:SOFT2412-2021S2/W16_R5_Group1_Assignment2.git
```

## Usage

### Setup

In the configuration file cinemasystem, you can set the mode of starting the software. At the same time, you can set a standard price for the ticket price, and then set the ticket price by setting the corresponding percentage of different kinds of tickets.
You can use the data in the cinema data base.

### Run

You could use gradle run to run this program.


**Customer mode:**
1.	You can choose what service you need including register, login, book movie, log out and exit.
2.	Register: you can register with 9 characters number for user id, and the password need to less than 50. Also, if you try to register the user id which is already in database, you will file.
3.	Login: if you are not login, you can choose this service to login.
4.	Book movie: you can book movie without login until payment part. And before payment, the system would ask you to login or register. Also, in this part, you can choose the movie if want watch with any session, cinemas, in this week. Also, user can filter information by screen size and cinemas. Also, you can use bank card and gift card to buy the tickets.
5.	Logout: logout the account, then you can login another account.
6.	Exit: exit this system.

**Staff mode:**
1. Add delete and modify movie data.
2. Set the projection hall which we called screening in this project.
3. Add gift card to the database.
4. Output a csv file of the movies information which is not release yet.
5. Output a csv file of the information of every session in database.

**Manager mode:**
1. All permissions of staff.
2. Add and delete the staff account.
3. Output the csv file of information of transactions which are canceled by users.  

### Test

You could use **gradle test** to test whether the software can run normally in your local area.


## Maintainers
[@YuchengPeng]
[@HuirongGuo]

[@YuchengPeng]: https://github.sydney.edu.au/ypen9276
[@HuirongGuo]: https://github.sydney.edu.au/hguo0780

## Contributors

<a href="https://github.sydney.edu.au/SOFT2412-2021S2/W16_R5_Group1_Assignment1/graphs/contributors"><img src="contributor.png"></a>


## License

MIT © [Yucheng Peng](https://github.sydney.edu.au/ypen9276)
