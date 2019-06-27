# Interthread-Communication-with-TCP-sockets
This solution is aimed to achieved inter thread communication with the help of TCP connection.

An example customers.txt file would be:
{jill,450}.
{joe,157}.
{bob,100}.
{sue,125}.
An example banks.txt file would be:
{rbc,800}.
{bmo,700}.
{ing,200}.

The customer and bank tasks will then start up and wait for contact (you may want to make each
new task sleep for a 100 milliseconds or so, just to make sure that all tasks have been created and
are ready to be used). So the banking mechanism works as follows:
1. Each customer wants to borrow the amount listed in the input file. At any one time,
however, they can only request a maximum of 50 dollars. When they make a request, they
will therefore choose a random dollar amount between 1 and 50 for their current loan.
2. When they make a request, they will also randomly choose one of the banks as the target.
3. Before each request, a customer will wait/sleep a random period between 10 and 100
milliseconds. This is just to ensure that one customer doesn’t take all the money from the
banks at once.
4. So the customer will make the request and wait for a response from the bank. It will not
make another request until it gets a reply about the current request.
5. The bank can accept or reject the request. It will reject the request if the loan would reduce
its current financial resources below 0. Otherwise, it grants the loan and notifies the
customer.
6. If the loan is granted, the customer will deduct this amount from its total loan requirement
and then randomly choose a bank (possibly the same one) and make another request
(again, between 1 and 50 dollars).
7. If the loan is rejected, however, the customer will remove that bank from its list of potential
lenders, and then submit a new request to the remaining banks.
8. This process continues until customers have either received all of their money or they have
no available banks left to contact.
And that’s it.
