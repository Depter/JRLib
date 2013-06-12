# JRLib

The target of the project is to create a Non-Life reserving software, similiar in 
capabilities and speed to the commercial versions. The software will be built on top of
the Netbeans platform.

## Technical contents
The following sections describe the technical content of the project.

### Estimates
The following estimation methods are implemented in the current state:
1.  Average-Claim
2.  Expected-Loss-Ratio
3.  Bornhuetter-Ferguson
4.  Cape-Cod
5.  Chain-Ladder
6.  Mack's method (analytic standard errors)
7.  Munich Chain-Ladder
8.  Composite estimate, that enables to use of different method for different accident periods

### Tests
Some tests in regard with the assumptions underlying the
Chain-LAdder method, suggested by Mack, are also implemented:
1.  Test for calendar year effect.
2.  Test for independence of subsequent development factors

### Bootstrap
The most popular bootstrapping methods are also implemented:
1.  Overdispersed Poisson Model
2.  Mack's model
3.  Bootstrap for the Munich Chain-Ladder method

### Projects
This part describes the main projects, bulding blocks, of
JReserve:
1.  JRLib: this is a standalone, pure Java, library which
    defines all the calculations (estimates, tests, 
    bootstraps etc). All other projects are providing 
    (user-)interfaces to the functionality, defined in this
    project. If exploring the repository, look here first).
2.  GRScript: defines a very simple, expandable Groovy DSL 
    to JRLib. The concept of the project is to define
    delegates for each concept within JRLib (i.e a ClaimTriangle
    or LinkRatio), and make these concepts easily accessible
    with builders in a Groovy script.
3.  GRScript-GUI: the project provides a simple editor, with
    plotting and help functionalities to grscripts.
4.  JReserve-Dummy: This project is a dummy gui, to test and
    showcase concepts for the final application.

## Repository Sctructure
This is a short overwiev of the directories and their contents
of the repository.
> JRLib
>  |- Design
>  |    Contains the design for the project. Not yet completed.
>  |- ExcelCalc
>  |    Contains the excel example calculations. Mainly used in 
>  |    the Unit Tests.
>  |- Literature
>  |    Contains the used literature (mainly PDF), describing the 
>  |    technical background of the different methods.
>  |- Misc
>  |    Contains miscellaneous files, such as icons for the 
>  |    applications.
>  |- NetbeansProject
>      |- jrlib
>      |    This is the first netbeans project to look at. It is the 
>      |    core calculation library, containing all the implementations
>      |    of the different methods, tests and bootstraps.
>      |- grscript
>      |    This project provides a simple DSL for JRLib, implemented 
>      |    in Groovy. The DSL enables quick testing of new methods
>      |    and concepts.
>      |- grscript-gui
>      |    Provides a simple editor to GRScript with additional
>      |    plotting and outputwindow as a Netbeans RCP.
>      |- jreserve-dummy
>      |    Dummy Netbeans RCP, to showcase the different windows,
>      |    settings of the final application.



