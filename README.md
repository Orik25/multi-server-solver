# multi-server-solver
The main purpose of this application is to process time-consuming requests from customers.Examples of such requests can be tasks: solving a system of equations, the finite element method, pattern recognition, image processing, and others(In this case, the service is designed to recursively find the Fibonacci number by its index).
This service is built on a microservice architecture where there are 3 types of servers (user server, load balancer server and application server)

How it works

    1.The client sends a request to the user-server, 
    
    2.Request is immediately recorded in the client's request history

    3.The user-server sends a request to the load balancer to get the port on which the server is least loaded

    4.The load balancer contacts each of the working servers to get statistics on each of them according to their workload

    5.The load balancer processes all the statistics it received earlier and determines the port to which the client`s
    request should be sent and sends a response to the user-server

    6.The user-server sends a client request to a specific application server and receives the task status in response.

    7.Then it records the status in the database

    8.User-server returns a response to the client with the statuses of its tasks
    
<div class="center">
  <img width="417" alt="project" src="https://github.com/Orik25/multy-server-solver/assets/92794561/c4956a35-d307-495e-88cf-3c2260d38416">
</div>


Features:
- The request has a maximum labor intensity (in case of exceeding - refusal of execution)
- The client is informed how much time is left until the completion of his task
- The client can cancel the execution of the task or send another task so that they are executed in parallel
- The client needs to be authorized to use the service(with using JWT token).
- The client has access to the history of his requests
- The service uses a load balancer to evenly distribute tasks between servers(It is also optimized in such a way that with each user request it does not receive statistics on all servers again, but keeps the same one for a certain number of requests from clients, and if the thread pool or queue is full on any server, the application server notifies the load balancer about this)
- In case when all servers are busy, the client is informed that his request has been queued. And if all queues are busy, then the request is canceled due to the inability of the service to process it

Roles:
- Client(Sends calculation requests and receives a response)
- VIP(Separate application servers are allocated for this type of client to perform calculations)
- Admin(Can view all running processes and monitor system servers load.)
  
