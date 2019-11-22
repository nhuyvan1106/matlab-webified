# matlab-webified
[Work Project] An example application that demonstrates how to compile MATLAB code to Java and how to integrate MATLAB into
a Java web application

MATLAB Compiler Runtime (MCR) for Java implements a process-wide blocking mechanism, each time a MATLAB script that is 
compiled to Java is invoked, subsequent invocations are blocked until the current invocation completes, so it is not very
ideal to integrate MATLAB scripts compiled to Java into a web application. To work around this problem, each time a request
to invoke a MATLAB script is received, the backend packages it up into a Task object, and sends it as a remote object using
Java's technology called Remote Method Invocation (RMI) for processing, once the processing is done, the result is sent back to the
invoking process which is the application server process. This way, the application server is always available for accepting new requests.
