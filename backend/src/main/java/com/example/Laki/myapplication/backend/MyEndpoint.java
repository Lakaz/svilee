/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.example.Laki.myapplication.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.GeoPoint;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.PutException;
import com.google.appengine.api.search.QueryOptions;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchException;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.search.SortExpression;
import com.google.appengine.api.search.SortOptions;
import com.google.appengine.api.search.StatusCode;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Named;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import static com.example.Laki.myapplication.backend.Constants.ENTITY_ITEM;
import static com.example.Laki.myapplication.backend.Constants.ENTITY_KIND_TOKENS;
import static com.example.Laki.myapplication.backend.Constants.ENTITY_REACT_RECEIVED_ITEMS;
import static com.example.Laki.myapplication.backend.Constants.ENTITY_RECEIVED_ITEMS;
import static com.example.Laki.myapplication.backend.Constants.GIVER_AVATAR_URL;
import static com.example.Laki.myapplication.backend.Constants.GIVER_UUID;
import static com.example.Laki.myapplication.backend.Constants.ITEM_AGE_GROUP_KEY;
import static com.example.Laki.myapplication.backend.Constants.ITEM_CATEGORY_KEY;
import static com.example.Laki.myapplication.backend.Constants.ITEM_DESCRIPTION_KEY;
import static com.example.Laki.myapplication.backend.Constants.ITEM_GIVER_NAME;
import static com.example.Laki.myapplication.backend.Constants.ITEM_ID;
import static com.example.Laki.myapplication.backend.Constants.ITEM_IMAGE_URL;
import static com.example.Laki.myapplication.backend.Constants.ITEM_LOCATION;
import static com.example.Laki.myapplication.backend.Constants.ITEM_STATUS;
import static com.example.Laki.myapplication.backend.Constants.LOCATION_KEY;
import static com.example.Laki.myapplication.backend.Constants.PICK_LOCATION_KEY;
import static com.example.Laki.myapplication.backend.Constants.PICK_TIME_KEY;
import static com.example.Laki.myapplication.backend.Constants.RECEIVER_KEY;
import static com.example.Laki.myapplication.backend.Constants.RECEIVER_UUID;
import static com.example.Laki.myapplication.backend.Constants.TIME_POSTED_KEY;
import static com.example.Laki.myapplication.backend.Constants.TIME_RECEIVED_KEY;
import static com.example.Laki.myapplication.backend.Constants.TITLE_KEY;
import static com.example.Laki.myapplication.backend.Constants.TOKEN_KEY;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "myApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.myapplication.Laki.example.com",
                ownerName = "backend.myapplication.Laki.example.com",
                packagePath = ""
        )
)
public class MyEndpoint {

    /**
     * A simple endpoint method that takes a name and says Hi back
     */
    @ApiMethod(name = "sayHi")
    public MyBean sayHi(@Named("name") String name) {
        MyBean response = new MyBean();
        response.setData("Hi, " + name);

        try {
            InputStream is = new FileInputStream(
                    new File("WEB-INF/socialexchange-a3520b7fe2fc.json"));
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setServiceAccount(is)
                    .setDatabaseUrl("https://socialexchange-a7a20.firebaseio.com/")
                    .build();

            FirebaseApp.initializeApp(options);

        } catch (Exception e) {
            //logger.log(Level.SEVERE, e.toString(), e);
            e.printStackTrace();
        }


        return response;
    }


    @ApiMethod(name = "clearListings")
    public SuccessResponse clearListings(){

        String allQuery = "distance(geopoint(" + -33.836143 + "," + 18.733181 + ")," + LOCATION_KEY + ") < 5000 ";

        SuccessResponse response = new SuccessResponse("false");

        Date currentDate = new Date();
        Date date = null;
        long timeDiff = 0L;
        try {
            date = new SimpleDateFormat("yyyyMMdd_HHmmss").parse(TIME_POSTED_KEY);
            timeDiff = currentDate.getTime() - date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String sort = Long.toString(timeDiff);


        try {

            SortOptions sortOptions = SortOptions.newBuilder()
                    .addSortExpression(SortExpression.newBuilder()
                            .setExpression(sort)
                            .setDirection(SortExpression.SortDirection.ASCENDING)
                            .setDefaultValueNumeric(0))
                    .setLimit(200)
                    .build();

            QueryOptions options = QueryOptions.newBuilder()
                    .setLimit(50)
                    .setOffset(0)
                    .setSortOptions(sortOptions)

                    .build();


            com.google.appengine.api.search.Query queryIndex = com.google.appengine.api.search.Query.newBuilder().setOptions(options).build(allQuery);


            IndexSpec indexSpec = IndexSpec.newBuilder().setName(ENTITY_ITEM).build();
            Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
            Results<ScoredDocument> results = index.search(queryIndex);


            for (ScoredDocument document : results) {
                String id = document.getId();
                deleteListing(id);
            }
            response.setResponse("success");
        }

        catch (SearchException e) {
            e.printStackTrace();
            response.setResponse("failure");
        }

        return response;



    }


    @ApiMethod(name = "deleteItem")
    public SuccessResponse deleteItem(@Named("itemId") String itemId) {
        String response = deleteListing(itemId);
        return new SuccessResponse(response);
    }

    @ApiMethod(name = "updateItem")
    public SuccessResponse updateItem(
            @Named("itemId") String itemId,
            @Named("giverName") String giverName,
            @Named("itemDescription") String itemDescription,
            @Named("itemAgeGroup") String itemAgeGroup,
            @Named("itemCategory") String itemCategory,
            @Named("itemImage") String itemImageUrl,
            @Named("giverAvatarUrl") String giverAvatarUrl,
            @Named("giverUUID") String giverUUID,
            @Named("itemStatus") String itemStatus,
            @Named("placeName") String placeName,
            @Named("latitude") float latitude,
            @Named("longitude") float longitude){

        String deleteResult = deleteListing(itemId);

        SuccessResponse response = new SuccessResponse("");

        if (deleteResult.equals("success")) {

            String timePost = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
            GeoPt geoPt = new GeoPt(latitude, longitude);
            Entity post = new Entity(ENTITY_ITEM);
            post.setProperty(ITEM_ID, itemId);
            post.setProperty(ITEM_GIVER_NAME, giverName);
            post.setProperty(ITEM_DESCRIPTION_KEY, itemDescription);
            post.setProperty(ITEM_AGE_GROUP_KEY, itemAgeGroup);
            post.setProperty(ITEM_CATEGORY_KEY, itemCategory);
            post.setProperty(ITEM_IMAGE_URL, itemImageUrl);
            post.setProperty(ITEM_STATUS, itemStatus);
            post.setProperty(GIVER_AVATAR_URL, giverAvatarUrl);
            post.setProperty(TIME_POSTED_KEY, timePost);
            post.setProperty(GIVER_UUID, giverUUID);
            post.setProperty(LOCATION_KEY, geoPt);
            post.setProperty(ITEM_LOCATION, placeName);


            datastoreService.put(post);



            Document doc = Document.newBuilder()
                    .setId(itemId)
                    .addField(Field.newBuilder().setName(ITEM_GIVER_NAME).setText(giverName))
                    .addField(Field.newBuilder().setName(ITEM_DESCRIPTION_KEY).setText(itemDescription))
                    .addField(Field.newBuilder().setName(ITEM_AGE_GROUP_KEY).setText(itemAgeGroup))
                    .addField(Field.newBuilder().setName(ITEM_CATEGORY_KEY).setText(itemCategory))
                    .addField(Field.newBuilder().setName(ITEM_IMAGE_URL).setText(itemImageUrl))
                    .addField(Field.newBuilder().setName(ITEM_STATUS).setText(itemStatus))
                    .addField(Field.newBuilder().setName(GIVER_AVATAR_URL).setText(giverAvatarUrl))
                    .addField(Field.newBuilder().setName(TIME_POSTED_KEY).setText(timePost))
                    .addField(Field.newBuilder().setName(GIVER_UUID).setText(giverUUID))
                    .addField(Field.newBuilder().setName(ITEM_LOCATION).setText(placeName))
                    .addField(Field.newBuilder().setName(LOCATION_KEY).setGeoPoint(new GeoPoint(latitude, longitude)))
                    .build();


            IndexSpec indexSpec = IndexSpec.newBuilder().setName(ENTITY_ITEM).build();
            Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);

            final int maxRetry = 3;
            int attempts = 0;
            int delay = 2;
            while (true) {
                try {
                    index.put(doc);
                    response.setResponse("success");
                } catch (PutException e) {
                    if (StatusCode.TRANSIENT_ERROR.equals(e.getOperationResult().getCode()) && ++attempts < maxRetry) { // retrying
                        try {
                            Thread.sleep(delay * 1000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        delay *= 2; // easy exponential backoff
                        continue;
                    } else {
                        response.setResponse("failed");
                        throw e; // otherwise throw

                    }
                }
                break;
            }

            return response;
        }
        else {
            response.setResponse("failure");
            return response;
        }
    }




    private String deleteListing(String id) {

        final int maxRetry = 3;
        int attempts = 0;
        int delay = 2;

        while (true) {

            try {
                IndexSpec indexSpec = IndexSpec.newBuilder().setName(ENTITY_ITEM).build();
                Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
                index.delete(id);
                return  "success";
            }

            catch (Exception e) {

                e.printStackTrace();

                if (++attempts < maxRetry) { // retrying
                    try {
                        Thread.sleep(delay * 1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    delay *= 2; // easy exponential backoff
                    continue;
                }


                else {
                    //response.setResponse("failed");
                    //throw e; // otherwise throw
                    return "failed";
                }
            }

        }


    }





    @ApiMethod(name = "postItem")
    public SuccessResponse postItem(
            @Named("itemId") String itemId,
            @Named("giverName") String giverName,
            @Named("itemDescription") String itemDescription,
            @Named("itemAgeGroup") String itemAgeGroup,
            @Named("itemCategory") String itemCategory,
            @Named("itemImage") String itemImageUrl,
            @Named("giverAvatarUrl") String giverAvatarUrl,
            @Named("giverUUID") String giverUUID,
            @Named("itemStatus") String itemStatus,
            @Named("placeName") String placeName,
            @Named("latitude") float latitude,
            @Named("longitude") float longitude,
            @Named("pickLocation") String pickLocation,
            @Named("pickTime") String pickTime,
            @Named("title") String title){


        String timePost = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());


        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        GeoPt geoPt = new GeoPt(latitude, longitude);
        Entity post = new Entity(ENTITY_ITEM);
        post.setProperty(ITEM_ID, itemId);
        post.setProperty(ITEM_GIVER_NAME, giverName);
        post.setProperty(ITEM_DESCRIPTION_KEY, itemDescription);
        post.setProperty(ITEM_AGE_GROUP_KEY, itemAgeGroup);
        post.setProperty(ITEM_CATEGORY_KEY, itemCategory);
        post.setProperty(ITEM_IMAGE_URL, itemImageUrl);
        post.setProperty(ITEM_STATUS, itemStatus);
        post.setProperty(GIVER_AVATAR_URL, giverAvatarUrl);
        post.setProperty(TIME_POSTED_KEY, timePost);
        post.setProperty(GIVER_UUID, giverUUID);
        post.setProperty(LOCATION_KEY, geoPt);
        post.setProperty(ITEM_LOCATION, placeName);
        post.setProperty(PICK_LOCATION_KEY, pickLocation);
        post.setProperty(PICK_TIME_KEY, pickTime);
        post.setProperty(TITLE_KEY, title);


        datastoreService.put(post);

        SuccessResponse response = new SuccessResponse("");

        Document doc = Document.newBuilder()
                .setId(itemId)
                .addField(Field.newBuilder().setName(ITEM_ID).setText(itemId))
                .addField(Field.newBuilder().setName(ITEM_GIVER_NAME).setText(giverName))
                .addField(Field.newBuilder().setName(ITEM_DESCRIPTION_KEY).setText(itemDescription))
                .addField(Field.newBuilder().setName(ITEM_AGE_GROUP_KEY).setText(itemAgeGroup))
                .addField(Field.newBuilder().setName(ITEM_CATEGORY_KEY).setText(itemCategory))
                .addField(Field.newBuilder().setName(ITEM_IMAGE_URL).setText(itemImageUrl))
                .addField(Field.newBuilder().setName(ITEM_STATUS).setText(itemStatus))
                .addField(Field.newBuilder().setName(GIVER_AVATAR_URL).setText(giverAvatarUrl))
                .addField(Field.newBuilder().setName(TIME_POSTED_KEY).setText(timePost))
                .addField(Field.newBuilder().setName(GIVER_UUID).setText(giverUUID))
                .addField(Field.newBuilder().setName(ITEM_LOCATION).setText(placeName))
                .addField(Field.newBuilder().setName(PICK_LOCATION_KEY).setText(pickLocation))
                .addField(Field.newBuilder().setName(PICK_TIME_KEY).setText(pickTime))
                .addField(Field.newBuilder().setName(LOCATION_KEY).setGeoPoint(new GeoPoint(latitude, longitude)))
                .addField(Field.newBuilder().setName(TITLE_KEY).setText(title))
                .build();



        IndexSpec indexSpec = IndexSpec.newBuilder().setName(ENTITY_ITEM).build();
        Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);

        final int maxRetry = 3;
        int attempts = 0;
        int delay = 2;
        while (true) {
            try {
                index.put(doc);
                response.setResponse("success");
            }

            catch (PutException e) {
                if (StatusCode.TRANSIENT_ERROR.equals(e.getOperationResult().getCode()) && ++attempts < maxRetry) { // retrying
                    try {
                        Thread.sleep(delay * 1000);
                    }

                    catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    delay *= 2; // easy exponential backoff
                    continue;
                }

                else {
                    response.setResponse("failed");
                    throw e; // otherwise throw

                }
            }
            break;
        }

        return response;
    }


    @ApiMethod(name = "checkItemStatus")
    public ItemStatusResponse checkItemStatus(@Named("itemId") String itemId) {

        Query query = new Query(ENTITY_REACT_RECEIVED_ITEMS);
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();

        query.setFilter(new Query.FilterPredicate(
                ITEM_ID,
                Query.FilterOperator.EQUAL,
                itemId
        ));

        PreparedQuery preparedQuery = datastoreService.prepare(query);



        int totalCount = datastoreService.prepare(query).countEntities(FetchOptions.Builder.withDefaults());

        if (totalCount == 0) {
            ItemStatusResponse itemStatusResponse = new ItemStatusResponse();
            itemStatusResponse.setTaken(0);
            return itemStatusResponse;
        }

        Iterable<Entity> results = preparedQuery.asIterable();

        ItemStatusResponse statusResponse = new ItemStatusResponse();

        for (Entity result : results) {
            String recieverName = (String) result.getProperty(RECEIVER_KEY);
            String timeTaken = (String) result.getProperty(TIME_RECEIVED_KEY);
            String takeUUID = (String) result.getProperty(RECEIVER_UUID);
            statusResponse.setTaken(1);
            statusResponse.setTakerName(recieverName);
            statusResponse.setTakerUUID(takeUUID);
            statusResponse.setTimeTaken(timeTaken);
            break;
        }

        return statusResponse;
    }



    @ApiMethod(name = "receiveReactItem")
    public SuccessResponse receiveReactItem(@Named("itemId") String itemId, @Named("receicerName") String receiverName,
                                            @Named("receiverUUID") String receiverUUID ){

        SuccessResponse successResponse = new SuccessResponse("failure");

        String myQuery = ITEM_ID + "=" + itemId +" AND "+ITEM_STATUS+ "=" + "available";


        try {

            SortOptions sortOptions = SortOptions.newBuilder()
                    .setLimit(200)
                    .build();

            QueryOptions options = QueryOptions.newBuilder()
                    .setLimit(200)
                    .setSortOptions(sortOptions)
                    .build();


            com.google.appengine.api.search.Query queryIndex = com.google.appengine.api.search.Query.newBuilder().setOptions(options).build(myQuery);


            IndexSpec indexSpec = IndexSpec.newBuilder().setName(ENTITY_ITEM).build();
            Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
            Results<ScoredDocument> results = index.search(queryIndex);

            long docsFound = results.getNumberFound();

            if (docsFound > 0) {




                successResponse.setResponse("success");

                for (ScoredDocument document : results) {
                    String status = document.getOnlyField(ITEM_STATUS).getText();
                    String giverName = document.getOnlyField(ITEM_GIVER_NAME).getText();
                    String itemDescription = document.getOnlyField(ITEM_DESCRIPTION_KEY).getText();
                    String itemCategory = document.getOnlyField(ITEM_CATEGORY_KEY).getText();
                    String itemAgeGroup = document.getOnlyField(ITEM_AGE_GROUP_KEY).getText();
                    String timePosted = document.getOnlyField(TIME_POSTED_KEY).getText();
                    String itemImageUrl = document.getOnlyField(ITEM_IMAGE_URL).getText();
                    String giverUrl = document.getOnlyField(GIVER_AVATAR_URL).getText();
                    String giveUUID = document.getOnlyField(GIVER_UUID).getText();
                    String location = document.getOnlyField(ITEM_LOCATION).getText();

                    String timePost = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());


                    DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();

                    Entity post = new Entity(ENTITY_REACT_RECEIVED_ITEMS);

                    post.setProperty(ITEM_GIVER_NAME, giverName);
                    post.setProperty(RECEIVER_KEY, receiverName);
                    post.setProperty(ITEM_ID, itemId);
                    post.setProperty(TIME_RECEIVED_KEY, timePost);
                    post.setProperty(GIVER_UUID, giveUUID);
                    post.setProperty(RECEIVER_UUID, receiverUUID);


                    datastoreService.put(post);

                    break;
                }

                try {
                    index.delete(itemId);
                }

                catch (RuntimeException e){
                    e.printStackTrace();
                }

            }

        }

        catch (SearchException e) {
            e.printStackTrace();
        }

        return successResponse;

    }



    @ApiMethod(name = "receiveItem")
    public SuccessResponse receiveItem(@Named("itemId") String itemId, @Named("receicerName") String receiverName){

                SuccessResponse successResponse = new SuccessResponse("failure");

        String myQuery = ITEM_ID + "=" + itemId +" AND "+ITEM_STATUS+ "=" + "available";


        try {

            SortOptions sortOptions = SortOptions.newBuilder()
                    .setLimit(200)
                    .build();

            QueryOptions options = QueryOptions.newBuilder()
                    .setLimit(200)
                    .setSortOptions(sortOptions)
                    .build();


            com.google.appengine.api.search.Query queryIndex = com.google.appengine.api.search.Query.newBuilder().setOptions(options).build(myQuery);


            IndexSpec indexSpec = IndexSpec.newBuilder().setName(ENTITY_ITEM).build();
            Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
            Results<ScoredDocument> results = index.search(queryIndex);

            long docsFound = results.getNumberFound();

            if (docsFound > 0) {




                successResponse.setResponse("success");

                for (ScoredDocument document : results) {
                    String status = document.getOnlyField(ITEM_STATUS).getText();
                    String giverName = document.getOnlyField(ITEM_GIVER_NAME).getText();
                    String itemDescription = document.getOnlyField(ITEM_DESCRIPTION_KEY).getText();
                    String itemCategory = document.getOnlyField(ITEM_CATEGORY_KEY).getText();
                    String itemAgeGroup = document.getOnlyField(ITEM_AGE_GROUP_KEY).getText();
                    String timePosted = document.getOnlyField(TIME_POSTED_KEY).getText();
                    String itemImageUrl = document.getOnlyField(ITEM_IMAGE_URL).getText();
                    String giverUrl = document.getOnlyField(GIVER_AVATAR_URL).getText();
                    String giveUUID = document.getOnlyField(GIVER_UUID).getText();
                    String location = document.getOnlyField(ITEM_LOCATION).getText();

                    String timePost = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());


                    DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();

                    Entity post = new Entity(ENTITY_RECEIVED_ITEMS);

                    post.setProperty(ITEM_GIVER_NAME, giverName);
                    post.setProperty(RECEIVER_KEY, receiverName);
                    post.setProperty(ITEM_ID, itemId);
                    post.setProperty(TIME_RECEIVED_KEY, timePost);

                    datastoreService.put(post);

                    break;
                }

                try {
                    index.delete(itemId);
                }

                catch (RuntimeException e){
                    e.printStackTrace();
                }

            }

        }

        catch (SearchException e) {
            e.printStackTrace();
        }

        return successResponse;

    }

    @ApiMethod(name = "getItemListings")
    public CollectionResponse<Item> getItemListings(
            @Named("ageGroup") String ageGroup,
            @Named("category") String category,
            @Named("latitude") float latitude,
            @Named("longitude") float longitude,
            @Named("pageNumber") int pageNumber){


        int offset = (pageNumber) * 20;
        int nextPage = 0;

        int numberOfPages = 0;

        String execQuery = "";

        String sortExpression = "distance(geopoint("+latitude+","+longitude+"),"+LOCATION_KEY+")";

        String allQuery = "distance(geopoint(" + latitude + "," + longitude + ")," + LOCATION_KEY + ") < 5000 ";

        String allAgeGroupQuery = "distance(geopoint(" + latitude + "," + longitude + ")," + LOCATION_KEY + ") < 5000 AND " + ITEM_CATEGORY_KEY + "=" + category;// +" AND "+ITEM_AGE_GROUP_KEY+ "=" + ageGroup;

        String allToys = "distance(geopoint(" + latitude + "," + longitude + ")," + LOCATION_KEY + ") < 5000 AND " + ITEM_AGE_GROUP_KEY + "=" + ageGroup;// +" AND "+ITEM_AGE_GROUP_KEY+ "=" + ageGroup;


        String myQuery = "distance(geopoint(" + latitude + "," + longitude + ")," + LOCATION_KEY + ") < 5000 AND " + ITEM_CATEGORY_KEY + "=" + category +" AND "+ITEM_AGE_GROUP_KEY+ "=" + ageGroup;


        if (ageGroup.equals("all") && category.equals("all")) {
            execQuery = allQuery;
        }

        else if(!ageGroup.equals("all") && !category.equals("all")) {
            execQuery = myQuery;
        }
        else if (ageGroup.equals("all")) {
            execQuery = allAgeGroupQuery;
        }else {
            execQuery = allToys;
        }


        List<Item> prifileList = new ArrayList<>(0);

        Date currentDate = new Date();
        Date date = null;
        long timeDiff = 0L;
        try {
            date = new SimpleDateFormat("yyyyMMdd_HHmmss").parse(TIME_POSTED_KEY);
            timeDiff = currentDate.getTime() - date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String sort = Long.toString(timeDiff);

        //String myQuery = "distance(geopoint("+latitude+","+longitude+"),"+LOCATION_KEY+") < 1500";// AND "+INTEREST_KEY +"="+interest;

        try {

            SortOptions sortOptions = SortOptions.newBuilder()
                    .addSortExpression(SortExpression.newBuilder()
                            .setExpression(sort)
                            .setDirection(SortExpression.SortDirection.ASCENDING)
                            .setDefaultValueNumeric(0))
                    .setLimit(200)
                    .build();

            QueryOptions options = QueryOptions.newBuilder()
                    .setLimit(20)
                    .setOffset(offset)
                    .setSortOptions(sortOptions)

                    .build();


            com.google.appengine.api.search.Query queryIndex = com.google.appengine.api.search.Query.newBuilder().setOptions(options).build(execQuery);


            IndexSpec indexSpec = IndexSpec.newBuilder().setName(ENTITY_ITEM).build();
            Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
            Results<ScoredDocument> results = index.search(queryIndex);

            long docsFound =  results.getNumberFound();
            int remainder = (int) (docsFound % 20);
            numberOfPages = docsFound > 0 ? (int) (docsFound/20) : 0;
            numberOfPages = remainder > 0 ? (numberOfPages + 1) : numberOfPages;

            for (ScoredDocument document : results) {
                String giverName = document.getOnlyField(ITEM_GIVER_NAME).getText();
                String itemDescription = document.getOnlyField(ITEM_DESCRIPTION_KEY).getText();
                String itemCategory = document.getOnlyField(ITEM_CATEGORY_KEY).getText();
                String itemAgeGroup = document.getOnlyField(ITEM_AGE_GROUP_KEY).getText();
                String timePosted = document.getOnlyField(TIME_POSTED_KEY).getText();
                String itemImageUrl = document.getOnlyField(ITEM_IMAGE_URL).getText();
                String giverUrl = document.getOnlyField(GIVER_AVATAR_URL).getText();
                String giveUUID = document.getOnlyField(GIVER_UUID).getText();
                String location = document.getOnlyField(ITEM_LOCATION).getText();
                String status = document.getOnlyField(ITEM_STATUS).getText();
                GeoPoint geoPoint = document.getOnlyField(LOCATION_KEY).getGeoPoint();
                String pickTime = document.getOnlyField(PICK_TIME_KEY).getText();
                String pickLocation = document.getOnlyField(PICK_LOCATION_KEY).getText();
                String title = document.getOnlyField(TITLE_KEY).getText();
                String id = document.getId();

                Item item = new Item();
                item.setId(id);
                item.setGivenLocation(location);
                item.setItemDescription(itemDescription);
                item.setItemAgeGroup(itemAgeGroup);
                item.setItemCategory(itemCategory);
                item.setItemUrl(itemImageUrl);
                item.setGiverName(giverName);
                item.setGiverAvatarUrl(giverUrl);
                item.setGiverUUID(giveUUID);
                item.setTimePosted(timePosted);
                item.setItemStatus(status);
                item.setLatitude((float) geoPoint.getLatitude());
                item.setLongitude((float) geoPoint.getLongitude());
                item.setPickLocation(pickLocation);
                item.setPickTime(pickTime);
                item.setTitle(title);

                prifileList.add(item);

            }
        }

        catch (SearchException e) {
            e.printStackTrace();
        }

        return CollectionResponse.<Item> builder().setItems(prifileList).setNextPageToken(Integer.toString(numberOfPages)).build();

    }


    @ApiMethod(name = "serveRegistrationToken")
    public void serveRegistrationToken(@Named("userUUID") String userUUID, @Named("token") String token) {

        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();

        Query query = new Query(ENTITY_KIND_TOKENS);

        query.setFilter(new Query.FilterPredicate(GIVER_UUID, Query.FilterOperator.EQUAL, userUUID));
        PreparedQuery preparedQuery = datastoreService.prepare(query);
        Iterable<Entity> results = preparedQuery.asIterable();

        int count = 0;

        for (Entity entity : results) {
            entity.setProperty(TOKEN_KEY, token);
            count = count+1;
            datastoreService.put(entity);
        }

        if (count == 0) {
            Entity userToken = new Entity(ENTITY_KIND_TOKENS);
            userToken.setProperty(GIVER_UUID, userUUID);
            userToken.setProperty(TOKEN_KEY, token);
            datastoreService.put(userToken);

        }




    }


    @ApiMethod(name = "inviteToChat")
    public ChatRequestResponse inviteToChat(@Named("chatroom") String chatroom, @Named("invitedUUID") String invitedUUID, @Named("senderName") String senderName) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();

        Query query = new Query(ENTITY_KIND_TOKENS);

        query.setFilter(new Query.FilterPredicate(
                GIVER_UUID,
                Query.FilterOperator.EQUAL,
                invitedUUID
        ));

        PreparedQuery preparedQuery = datastoreService.prepare(query);
        Iterable<Entity> results = preparedQuery.asIterable();

        String token = null;

        for (Entity result : results) {
            token = (String) result.getProperty(TOKEN_KEY);
        }

        if (token != null) {
            //return sendNotificationToChat(token, "Wants to connect with you", chatRoom );




            String uri = "https://fcm.googleapis.com/fcm/send";

            ChatRequestResponse chatRequestResponse = null;

            HttpClient client = getHttpClient();
            try {
                HttpPost postRequest = new HttpPost(uri);
                postRequest.setHeader("Authorization","key=AAAAvXCWDto:APA91bFTvHIW6kBnQuctsDrumHoxSXtvv5xTKrXp57J-QNPQbYM9e4Vo_mmtbC3mRD-RRVbPV6Q5RE_cLRGJ2keY8Wd5GHIqbBUZCFvcKSCz6Bg6PxOF_kJrLp_bHF1P3bok3QOUZIzd");
                postRequest.setHeader("Content-type", "application/json");

                JSONObject json = new JSONObject();
                json.put("to", token);
                JSONObject data = new JSONObject();

                data.put("title","Invitation to connect");   // Notification title
                data.put("body", senderName+ "Wants to connect with you");
                data.put("chatroom", chatroom);
                // Notification body
                //data.put("destitle", destitle);
                //data.put("url", url);
                //data.put("description", description);


                json.put("data", data);

                StringEntity entity = new StringEntity(json.toString());
                postRequest.setEntity(entity);

                try {

                    HttpResponse responsee = client.execute(postRequest);
                    InputStreamReader inputStreamReader = new InputStreamReader(responsee.getEntity().getContent());
                    BufferedReader rd = new BufferedReader(inputStreamReader);


                    //System.out.println("NOTIFICATION RESPONSE ----->"+msg1+msg2);
                    String line = "";
                    while((line = rd.readLine()) != null)
                    {

                        System.out.println(line);
                        return new ChatRequestResponse("success");
                    }


                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return new ChatRequestResponse("failure");


        }

        return new ChatRequestResponse("failure");

    }



    public HttpClient getHttpClient() {
        try {
            HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

            DefaultHttpClient client = new DefaultHttpClient();

            SchemeRegistry registry = new SchemeRegistry();
            SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
            socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
            registry.register(new Scheme("https", socketFactory, 443));
            SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
            DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());

            // Set verifier
            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

            // Example send http request

            return httpClient;
        } catch (Exception ex) {
            return HttpClientBuilder.create().build();
        }
    }


    public ChatRequestResponse sendNotificationToChat(@Named("receiverToken") String receiverToken, @Named("notification") String notification, @Named("chatroom") String chatroom) {

        String uri = "https://fcm.googleapis.com/fcm/send";

        ChatRequestResponse chatRequestResponse = null;

        HttpClient client = getHttpClient();
        try {
            HttpPost postRequest = new HttpPost(uri);
            postRequest.setHeader("Authorization","key=AAAAvXCWDto:APA91bFTvHIW6kBnQuctsDrumHoxSXtvv5xTKrXp57J-QNPQbYM9e4Vo_mmtbC3mRD-RRVbPV6Q5RE_cLRGJ2keY8Wd5GHIqbBUZCFvcKSCz6Bg6PxOF_kJrLp_bHF1P3bok3QOUZIzd");
            postRequest.setHeader("Content-type", "application/json");

            JSONObject json = new JSONObject();
            json.put("to", receiverToken);
            JSONObject data = new JSONObject();

            data.put("title","Invitation to connect");   // Notification title
            data.put("subtitle", notification);
            data.put("chatroom",chatroom);
            // Notification body
            //data.put("destitle", destitle);
            //data.put("url", url);
            //data.put("description", description);


            json.put("data", data);

            StringEntity entity = new StringEntity(json.toString());
            postRequest.setEntity(entity);

            try {

                HttpResponse responsee = client.execute(postRequest);
                InputStreamReader inputStreamReader = new InputStreamReader(responsee.getEntity().getContent());
                BufferedReader rd = new BufferedReader(inputStreamReader);


                //System.out.println("NOTIFICATION RESPONSE ----->"+msg1+msg2);
                String line = "";
                while((line = rd.readLine()) != null)
                {

                    System.out.println(line);
                    return new ChatRequestResponse("success");
                }


            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return new ChatRequestResponse("failure");

    }


}
