package testingInProgress;

import java.io.PrintWriter;

/**
 * This class simulates the HttpServletResponse object, which handles writing to the response and sending redirects.
 * Main class to simulate and test the commit and redirect behavior.
 */
public class CommitAndRedirectSimulation {

    public static void main(String[] args) {
        // Create a simulated HTTP response
        HttpServletResponseMock response = new HttpServletResponseMock();
        // Call the logic that mimics a real servlet behavior
        new CommitAndRedirectExample().doGet(response);
    }
}


class CommitAndRedirectExample {

    /**
     * Simulates the logic of handling an HTTP GET request, including writing content to
     * the response and attempting a redirect. If the response has been committed,
     * a redirection will throw an {@link IllegalStateException}.
     *
     * @param response The simulated HTTP response.
     */
    public void doGet(HttpServletResponseMock response) {
        // Write some content to the response (this will commit the response)
        PrintWriter out = response.getWriter();
        // out.println("This is some content that will be sent to the client.");

        // At this point, the response is committed (content is already sent to the client)

        // Try to send a redirect after the response has been committed

        response.sendRedirect("http://example.com");
        System.out.println("Redirect attempted.");
    }
}

/**
 * This class simulates the HttpServletResponse object, which handles writing to the response and sending redirects.
 * Simulates the {@link HttpServletResponse} object for testing purposes.
 * It mimics the behavior of response commitment and the ability to perform redirection.
 */
class HttpServletResponseMock {
    private boolean committed = false;
    private PrintWriter writer;

    /**
     * Constructor that initializes the mock response writer to print to the console.
     */
    public HttpServletResponseMock() {
        this.writer = new PrintWriter(System.out);  // Write to standard output for simulation
    }

    /**
     * Returns a {@link PrintWriter} for writing content to the response.
     * If the response hasn't been committed before, it marks the response as committed.
     *
     * @return The {@link PrintWriter} for writing content.
     */
    public PrintWriter getWriter() {
        if (!committed) {
            committed = true;  // Once content is written, the response is considered committed
        }
        return writer;
    }

    /**
     * Simulates sending a redirect response to the client.
     * If the response is already committed, an {@link IllegalStateException} is thrown.
     *
     * @param url The URL to redirect the client to.
     * @throws IllegalStateException if the response is already committed.
     */
    public void sendRedirect(String url) {
        if (committed) {
            throw new IllegalStateException("Cannot call sendRedirect() after the response has been committed.");
        }
        // In a real servlet, it would set the HTTP status and Location header here
        System.out.println("Redirecting to: " + url);
    }

    /**
     * Returns whether the response has already been committed (i.e., content has been written).
     *
     * @return True if the response has been committed, false otherwise.
     */
    public boolean isCommitted() {
        return committed;
    }
}
