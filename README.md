# Memory Manager

<br/>

A simple memory management application built with Java.

The purpose of this application is to simulate how a computer's memory is managed using three different algorithms.
<br/>
<br/>

**The algorithms are as followed:**

- First Fit
- Best Fit
- Worst Fit

<br/>

The program takes in a file as the input (command line argument). The text file `input_test` is formatted as follows:

**The first line is a number between 1 and 3:**

1 = First fit

2 = Best fit

3 = Worst fit

<br/>

The **second line** of the input has the **memory's total size** in KBs.

After the first two lines, each line is of one of these formats:

1. A PID MEMORY_SIZE

2. D PID

3. P

<br/>

The **first one** is a request for process with id: ***PID*** to be ***allocated***

***Size*** of the memory required for this process is: ***MEMORY_SIZE***.  

The unit would be KB.  

<br/>

The **second one** is ***deallocating*** memory for process with id ***PID***.  

<br/>

The **third one** asks the program to ***print out the current state of memory*** (allocations and free spaces).
