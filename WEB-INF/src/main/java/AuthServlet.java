import java.io.*;
import java.util.*;
import java.net.URL;
import java.security.GeneralSecurityException;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;


// @WebServlet(name = "WelcomeServlet", value = "/welcome-servlet")
public class AuthServlet extends HttpServlet {
    private static HashMap<String, String> registeredUsers = new HashMap<>();
    private static HashMap<String, String> oauthUsers = new HashMap<>();
    private String CLIENT_ID = "<-----GOOGLE CLIENT ID------>";
    private String CLIENT_SECRET = "<-----GOOGLE CLIENT SECRET------>";
    private String REDIRECT_URI = "http://localhost:8080/ShoppingApp/do-auth/g-auth-resp";

    public void init() {
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        URL url = new URL(request.getRequestURL().toString());
        String path = url.getPath();
        String currentRoute = path.split("/")[path.split("/").length-1];
        HttpSession session = request.getSession();
        if(session.getAttribute("username") != null) {
            response.sendRedirect("products");
        } else {
            if(currentRoute.equals("g-auth")) {
                doGoogleAuth(request, response);
            } else if(currentRoute.equals("g-auth-resp")) {
                handlePostGoogleAuth(request, response);
            } else {
                if(request.getParameter("Login") != null)
                    request.getRequestDispatcher("/WEB-INF/pages/login.html").forward(request, response);
                else if(request.getParameter("Register") != null)
                    request.getRequestDispatcher("/WEB-INF/pages/register.html").forward(request, response);
                else
                    request.getRequestDispatcher("/WEB-INF/pages/login.html").forward(request, response);
            }
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String reqType = request.getParameter("reqType");
            if(reqType.equals("reqLogin")) {
                doLogin(request, response);
            } else if(reqType.equals("reqReg")){
                doRegister(request, response);
            } else {
                request.getRequestDispatcher("/login.html?VE#").forward(request, response);
            }
        } catch(ServletException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void destroy() {
    }

    public void doLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if(registeredUsers.containsKey(username) && registeredUsers.get(username).equals(password)) {
            HashMap<String, String> user = new HashMap<>();
            user.put("username", username);
            HttpSession session = request.getSession();
            session.setAttribute("username", username);
            response.sendRedirect("products");
        } else {
            response.sendRedirect("do-auth?Login&InvalidUser=true");
        }
    }

    public void doRegister(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, InterruptedException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        if(registeredUsers.containsKey(username)) {
            response.sendRedirect("do-auth?Register&DuplicateRegistration=true");
            return;
        }
        if(password.equals(confirmPassword)) {
            registeredUsers.put(username, password);
            response.sendRedirect("do-auth?Login&Registered!=true");
        } else {
            response.sendRedirect("do-auth?Register&PasswordDoesn'tMatch=true");
        }
    }

    public void doGoogleAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            List<String> scopes = new ArrayList<>();
            scopes.add("https://www.googleapis.com/auth/userinfo.profile");
            scopes.add("https://www.googleapis.com/auth/userinfo.email");
            HttpTransport httpTransport = new NetHttpTransport();
            JacksonFactory jsonFactory = new JacksonFactory();
            GoogleAuthorizationCodeFlow authFlow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, 
                jsonFactory, 
                CLIENT_ID, 
                CLIENT_SECRET,
                scopes
            )
            .setApprovalPrompt("auto").build();

            String authUrl = authFlow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();
            response.sendRedirect(authUrl);
        } catch (Exception e) {
            response.setStatus(500);
            response.getWriter().println("Internal Server Error - ERCGA");
            throw new RuntimeException(e);
        }
    }

    public void handlePostGoogleAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            List<String> scopes = new ArrayList<>();
            scopes.add("https://www.googleapis.com/auth/userinfo.profile");
            scopes.add("https://www.googleapis.com/auth/userinfo.email");
            HttpTransport httpTransport = new NetHttpTransport();
            JacksonFactory jsonFactory = new JacksonFactory();
            GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                httpTransport,
                jsonFactory,
                CLIENT_ID,
                CLIENT_SECRET,
                request.getParameter("code"),
                REDIRECT_URI
            ).execute();
            String accessToken = tokenResponse.getAccessToken();
            String idToken = tokenResponse.getIdToken();
            // Credential credential = new Credential(BearerToken.authorizationHeaderAccessMethod()).setAccessToken(accessToken);
            GoogleIdToken.Payload userPayload = payloadParser(idToken, httpTransport, jsonFactory);
            HttpSession session = request.getSession();
            session.setAttribute("username", userPayload.getEmail());
            if(!oauthUsers.containsKey(userPayload.get("name"))) {
                userPayload.put(userPayload.getEmail(), userPayload.get("name"));
            }
            response.getWriter().println("Hello" + userPayload.get("name"));
            response.sendRedirect("../products");
        } catch (Exception ex) {
            response.setStatus(500);
            response.getWriter().println("Internal Server Error - ERCGA " + ex);
            throw new RuntimeException(ex);
        }
    }

    public GoogleIdToken.Payload payloadParser(String idToken, HttpTransport httpTransport, JacksonFactory jsonFactory) throws IOException, GeneralSecurityException {
        GoogleIdTokenVerifier googleIdTokenVerifier = new GoogleIdTokenVerifier(httpTransport, jsonFactory);
        GoogleIdToken token = GoogleIdToken.parse(jsonFactory, idToken);
        if (googleIdTokenVerifier.verify(token)) {
            GoogleIdToken.Payload payload = token.getPayload();
            if (!CLIENT_ID.equals(payload.getAudience())) {
                throw new IllegalArgumentException("Audience mismatch");
            } else if (!CLIENT_ID.equals(payload.getAuthorizedParty())) {
                throw new IllegalArgumentException("Client ID mismatch");
            }
            return payload;
        } else {
            throw new IllegalArgumentException("Cannot verify token!");
        }
    }
}