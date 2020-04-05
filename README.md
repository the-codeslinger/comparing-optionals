Comparison: Java java.util.Optional vs C++ std::optional
========================================================

The blog post accompanying this repository can be found on my website at [the-codeslinger.com][comparison].

These are a few sample projects investigating the use of optional types in different languages. This README files only explains how to get the samples running.

_Editor's note:_ Although the samples run, they are meant to be looked at as code snippets. The output is not really interesting and the code does nothing useful.

## Java

For Java I have chosen to use a simple Maven project with a single unit test file. It contains all the use cases I consider relevant from what I've required of optionals so far. Java is the Benchmark against which all other languages are compared because that's where I have the most experience.

To run the sample you require Java 11 and Maven installed on your computer. Then simply run the tests with Maven.

    $ mvn test

## C++ 

The C++ sample is even simpler. It consists of a single source file and a makefile, that's it. I elected to not use something like gtest in order to not have any dependencies that need to be managed manually (even if a package manager can help with that). The only thing required is a GCC (g++) version that supports C++17 optionals. If you prefer to use a different compiler then you need to modify the makefile accordingly or compile the code another way.

    $ make
    $ ./optional_test

[comparison](http://the-codeslinger.com/2020/04/05/comparing-java-optional-vs-c-stl-optional/)