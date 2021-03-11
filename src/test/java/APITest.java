import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
public class APITest {

    public void createNewBook(String bookName, String authorName){

        JSONObject bookObject = new JSONObject();
        bookObject.put("name",bookName);
        bookObject.put("author",authorName);

        given()
                .contentType("application/json")
                .body(bookObject.toString()).
        when()
                .post("/library").
        then().
                statusCode(201);
    }


    public void createNewUser(String firstName, String lastName){

        JSONObject user = new JSONObject();
        user.put("firstName",firstName);
        user.put("lastName", lastName);
        user.put("books",new JSONArray());
        given()
                .contentType("application/json").body(user.toString())
        .when()
                .post("/users").
        then().
                statusCode(201);

    }

    public void addBookToUser(String bookId, String userId){
        String book = get("/library/{id}", bookId).asString();
        JSONObject bookObject = new JSONObject(book);

        JSONArray bookArray = new JSONArray();
        bookArray.put(bookObject);

        JSONObject books = new JSONObject();
        books.put("books",bookArray);

        given().
                contentType("application/json").
                body(books.toString()).
        when().
                patch("/users/{userid}", userId).
        then().
                statusCode(200);

    }

    public void deleteUser(String userId){
        given().
                contentType("application/json").
        when()
                .delete("/users/{userid}", userId).
        then()
                .statusCode(200);
    }

    public void deleteBookFromLibrary(String bookId){

        given().
                contentType("application/json").
        when()
                .delete("/library/{bookid}", bookId).
        then().
                statusCode(200);
    }

    public void getUserThatDoesNotExist(String userId){
        when().
                get("/users/{userid}", userId).
        then().
                statusCode(404);
    }

    public void getBookThatDoesNotExist(String bookId){
        when().
                get("/library/{bookid}", bookId).
        then().
                statusCode(404);
    }

    public void clearUserLibrary(String userId){
        JSONArray booksArray = new JSONArray();

        JSONObject books = new JSONObject();
        books.put("books",booksArray);

        given().
                body(books.toString()).
                contentType("application/json").
        when().
                patch("/users/{userid}",userId).
        then().statusCode(200);
    }

    @Test
    public void apiJourney(){
        baseURI = "http://localhost:8080/";

        String userFirstName = "Michael";
        String userLastName = "Scofield";

        String bookName = "Yaban";
        String authorName = "Yakup Kadri Karaosmanoğlu";

        createNewBook(bookName,authorName);
        createNewUser(userFirstName,userLastName);
        addBookToUser("1","1"); // You can see users and books ID on the json server
        clearUserLibrary("1");
        deleteUser("1");
        deleteBookFromLibrary("1");
        getUserThatDoesNotExist("1");
        getBookThatDoesNotExist("1");



    }


}
