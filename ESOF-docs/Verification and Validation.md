#Verification and validation

## Degree of Testability

In the following topics Terasology's degree of testability will be discussed, focusing mainly on verification (which consists in ensuring that the product is being well built with the requirements obtained in mind) and validation (which consists in ensuring that the product will meet the intended purpose and the needs of its stakeholers).

###Controllability

Terasology implements unit testing to test both the engine and module components.

When the component under test is a module it's rather easy to control, since most modules are not dependant of anything else in order to run (even those that do, usually have no more that one or two dependencies), making their controllability not a problem.
 
However, in what concerns the engine component, the controllability is significantly lower, since it depends on several modules in order to successfully run - and consequently, be tested.

<a name="observability"/>
###Observability

As said in previous reports, Terasology uses [Jenkins](http://jenkins.terasology.org/) in order to automatically build and test every time a pull request is issued. If we check the [statistics](http://jenkins.terasology.org/view/Statistics/) page, we can see the test results of the various modules, namely which modules had failed tests, information regarding recent build history test results, and information regarding tests that are not directly related to JUnit tests, but also involve detecting eventual functional and structural defects in the modules. It also displays information regarding compiler warnings and line coverage on the JUnit tests.

The tests that are not directly related to JUnit tests include a "checkstyle" test, which evaluates the style/conventions used in the various modules, to ensure that the entire code is using the same style, a "findbugs" test, which scans for some common code drawbacks that may lead to bugs, PMD testing (which checks for common programming flaws, such as unused variables, unnecessary object creation, empty catch blocks when handling exceptions, among other things) and open task checking (which simply looks for TODO keywords in the code indicating what needs to be done).

In what regards the game's engine, the test results can be seen in [this](http://jenkins.terasology.org/job/Terasology/) page, more specifically in the graphs to the right. These graphs depict the trend for the latest builds in what concerns the tests described above in this subsection. If we click in a build number in the graph, we will get more detailed information for that test, in that build.

From this, we can easily assume that it is very easy to observe test results, since this information is conveniently displayed in a table and, if we wish to know more about something (for instance, about the warnings generated by a test), all we have to do is click on the link in the corresponding cell and we will be provided with more specific information regarding that specific test.

###Isolateability

Terasology doesn't implement unit tests in all modules, mostly because some modules really have not much of a reason to be tested (for instance, art-related modules) and because tests must be implemented by the module creators, and some don't implement them. This makes testing module components in isolation much more difficult.

Regarding the engine, the tests are implemented in a separate folder (called engine-tests). Although tests are usually kept in the same module, the fact that Terasology has some utility test classes that can be used by the modules, they decided to split them out in order to get the dependency resolution right (more specifically, module unit tests depend on engine tests which depends on the engine). This, coupled with the fact that the engine is heavily dependant on the modules to function, makes isolating the engine's components for testing rather difficult.

###Separation of concerns

The separation of concerns between engine/modules was already adressed in the [previous report](Software%20Architecture.md). Each module's function is well defined, and they are pretty much independent from each other (except when a module's function is really complex, and therefore depends on other modules). This approach is in our opinion correct since it allows for easier testing of a specific functionality.

###Understandability

Both Terasology's engine and its modules' code is well documented or self explanatory. The fact that Terasology keeps attracting new contributors, who need to understand the code in order to develop new features in the form of new modules, improve the current code or fix bugs, is a proof of the code's high understandability.

###Heterogeneity

Terasology uses several external libraries to run the game. The external libraries used can be seen in the [gradle build file](https://github.com/MovingBlocks/Terasology/blob/develop/engine/build.gradle#L94), used to compile the code. The main ones are:

- [LWJGL](https://www.lwjgl.org/). Stands for Lightweight Java Game Library, and it's exactly that - a library used to make games in Java.
- [JUnit](http://junit.org/). JUnit is the library used for unit testing.

Other libraries used, as seen in the build file above, are as follows:

- Storage and networking - "guava", "gson", "protobuf-java", "trove4j", "netty".
- Java-related libraries - "jna-platform", "reflections", "javassist", "reflectasm".
- Graphics, 3D, UI, etc - "lwjgl_util", "java3d", "abego.treelayout.core", "miglayout-core", "PNGDecoder".
- Logging and audio - "slf4j-api", "jorbis".
- Small-time 3rd party libraries - "MersenneTwister", "eaxy".

Besides those, other libraries created by MovingBlocks, the organization behind Terasology, are used: "gestalt-module", "gestalt-asset-core", "TeraMath", "tera-bullet", "splash-screen".

One of the main advantages of using external libraries is that their code is usually already tested and working, decreasing the need to produce code and consequently test it. However, this is not always true, since some libraries may still be in a beta stage of development and still have some bugs, which means that using them may cause issues. That is definitely not the case with the main libraries used by Terasology (LWJGL and JUnit), but for instance, libraries like the third party ones above may require some tests to be developed to ensure that they are working properly.

However, in what concerns Terasology, that doesn't seem to be a problem, since all the libraries used work without flaws and don't need testing in order to ensure they are doing its job properly.


## Test Statistics

The test statistics can be found in [Terasology's Jenkins statistics page](http://jenkins.terasology.org/view/Statistics/). As stated earlier, this page displays information regarding the game's engine and its modules. With this in mind, we get the following statistics:

- Number of unit tests: 2047.
- Engine line coverage: 25.96%
- Module with best line coverage: 93.65% (NanoMarkovChains).
- Module with worst line coverage (excluding modules without tests): 19.72% (TerasologyStable).
- Percentage of modules with implemented tests: 8.4%

Most of Terasology's tests are unit tests, although some performance tests can also be found. However, as we can see in the statistics above, a very significant part of the modules doesn't even implement tests. The main developers are aware of this and know that such low implementation of tests is harmful for the project (as we can see in [this](http://forum.terasology.org/threads/development-methodology-and-hi-students-from-porto.1387/) post), and have even set up an [issue](https://github.com/MovingBlocks/Terasology/issues/135) on Github regarding this problem, but it hasn't been sucessful until now, mostly because the contributors are constantly changing and most of them are not sufficiently familiarized with unit testing in order to develop tests for their modules.

In what concerns integration tests, they are made on average every few weeks by one of the main developers, [Cervator](https://github.com/Cervator), but this is highly erratic, since he only does that when he has spare time.

In order to ensure that the product is being built with the intended puropse and needs of the stakeholders (in other words, validation), everytime a release is made, the [forum](http://forum.terasology.org/) is used for them to provide feedback on what they think that needs to be made, what needs to be improved and what is following the correct path. The github [issue page](https://github.com/MovingBlocks/Terasology/issues/) is somewhat also used for this purpose, although its main use is for contributors to report bugs.

### Test case design strategy

Regarding test case design strategies, Terasology uses a white-box testing design stategy, since tests are gradually added to the project, with several kinds of tests to increase test strength (refer to the [observability](#observability) subsection of the previous section to see some of these tests). In our opinion, this is a correct approach due to constant changing of current contributors and the fact that contributions itself are made from their free time.


## Bug Report Solution

In order to further enhance our experience with the testing of Terasology, our group started to work in the [issue #1030](https://github.com/MovingBlocks/Terasology/issues/1030),
which was then considered obsolete by a Terasology developer and closed. But, while working on it we found a possible software
fault on the code and took advantage of Github's issue tracker to report it as [issue #2014](https://github.com/MovingBlocks/Terasology/issues/2014) and focused on solving it.

### Approach

Although fixing the software fault for this issue was relatively simple, in order to test it we developed a test suite which
not only tested the purpose of our fix but also other methods of the same class which weren't being tested.

### Submission

After solving the issue and ensuring that it passed all the tests, we created a [Pull Request](https://github.com/MovingBlocks/Terasology/pull/2017) to the original repository of Terasology and
associated it with the corresponding issue. Later on, the PR was verified and accepted by Terasology's
development team.
