https://www.interviewcake.com/question/java/two-egg-problem 
<br><br>
**We'll use _the first egg_ to get a _range of possible floors_** that contain the highest floor 
an egg can be dropped from without breaking. To do this, we'll drop it from increasingly higher
floors until it breaks, skipping some number of floors each time.

When the first egg breaks, **we'll use _the second egg_ to find the _exact_ highest floor** an egg
can be dropped from. We only have to drop the second egg starting from the last floor where the 
first egg didn't break, up to the floor where the first egg did break. But we have to drop the 
second egg one floor at a time.

With the first egg, if we skip the _same number of floors every time_, the worst case number 
of drops will increase by one _every_ time the first egg doesn't break. To counter this and 
keep our worst case drops _constant_, **we decrease the number of floors we skip _by one_ each time 
we drop the first egg**.

When we're choosing how many floors to skip the first time we drop the first egg, we know: <br>
1. **We want to skip as few floors as possible**, so if the first egg breaks right away we don't 
have a lot of floors to drop our second egg from.
2. **We _always_ want to be able to reduce the number of floors we're skipping**. We don't want to
get towards the top and not be able to skip floors any more.

Knowing this, we set up the following equation where nn is the number of floors we skip the first
time: <br> **n + (n−1) + (n−2) + … + 1 = 100**

This triangular series reduces to **n * (n + 1) / 2 = 100** which solves to give **n = 13.651**. 
We round up to **14** to be safe. So our first drop will be from the **14th** floor, our second will 
be **13** floors higher on the **27th** floor and so on until the first egg breaks. Once it breaks, 
we'll use the second egg to try every floor starting with the last floor where the first egg didn't
break. At worst, we'll drop both eggs a combined total of **_14_** times.

