/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.utils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import commons.*;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class ServerUtils {

    private String server = "http://localhost:8080/";


    private StompSession session;

    private static final ExecutorService EXEC = Executors.newSingleThreadExecutor();

    private boolean stop = true;


    //  http://localhost:8080   //


    public List<Board> getBoards(){
        if (server == null) return null;
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/boards/") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Board>>() {
                });
    }

    public Optional<Board> getLists() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/fellolists/") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Board>>() {})
                .stream()
                .findFirst();
    }

    public Task getTask(long taskId) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/tasks/" + taskId) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<Task>() {});
    }

    public Board getBoard(long boardId) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/boards/" + boardId) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<Board>() {});
    }

    public Task addTask(Task task, FelloList felloList) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/tasks/" + felloList.id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(task, APPLICATION_JSON), Task.class);
    }

    public Task editTask(Task task, long id) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/tasks/" + id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(task, APPLICATION_JSON), Task.class);
    }

    public Palette getPalette(long paletteId) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/palettes/" + paletteId) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<Palette>() {});
    }

    public Palette addPalette(Palette palette) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/palettes/") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(palette, APPLICATION_JSON), Palette.class);
    }

    public FelloList getFelloList(long listId) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/fellolists/" + listId) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<FelloList>() {});
    }

    public FelloList addFelloList(FelloList felloList) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/fellolists/") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(felloList, APPLICATION_JSON), FelloList.class);
    }

    public FelloList editFelloList(FelloList felloList, long id) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/fellolists/" + id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(felloList, APPLICATION_JSON), FelloList.class);
    }

    public Board addBoard(Board board) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/boards") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(board, APPLICATION_JSON), Board.class);
    }

    public Board editBoard(Board board) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/boards/" + board.id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(board, APPLICATION_JSON), Board.class);
    }

    public <T> void registerForMessages(String dest, Class<T> type, Consumer<T> consumer) {
        session.subscribe(dest, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return type;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                consumer.accept((T) payload);
            }
        });
    }


    public void send(String dest, Object o) {
        session.send(dest, o);
    }

    public StompSession connect(String url) {
        var client = new StandardWebSocketClient();
        //websocket

        var stomp = new WebSocketStompClient(client);
        //allows stomp messaging over websocket support

        stomp.setMessageConverter(new MappingJackson2MessageConverter());
        //converts messages from stomp to json

        try {
            return stomp.connect(url, new StompSessionHandlerAdapter() {
            }).get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Task deleteTask(long id) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/tasks/" + id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete(Task.class);
    }

    public FelloList deleteFelloList(long id) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/fellolists/" + id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete(FelloList.class);
    }

    public Board deleteBoard(long id) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/boards/" + id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete(Board.class);
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
        String address = server.substring(7);
        this.session = connect("ws://" + address + "/websocket");
    }

    public SubTask addSubtask(SubTask subTask, Task parentTask) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/subtasks/" + parentTask.id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(subTask, APPLICATION_JSON), SubTask.class);
    }

    public SubTask deleteSubTask(long id) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/subtasks/" + id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete(SubTask.class);
    }

    public SubTask editSubtask(SubTask subTask) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/subtasks/" + subTask.id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(subTask, APPLICATION_JSON), SubTask.class);
    }

    public Tags editTag(Tags tag) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/tags/" + tag.id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(tag, APPLICATION_JSON), Tags.class);
    }

    public Palette editPalette(Palette palette) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/palettes/" + palette.id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(palette, APPLICATION_JSON), Palette.class);
    }

    public Tags deleteTag(long id) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/tags/" + id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete(Tags.class);
    }

    public Palette deletePalette(long id) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/palettes/" + id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete(Palette.class);
    }


    public void registerForUpdates(Consumer<Board> consumer){

        EXEC.submit(() -> {
            while(stop){
                var res = ClientBuilder.newClient(new ClientConfig()) //
                        .target(server).path("api/boards/updates") //
                        .request(APPLICATION_JSON) //
                        .accept(APPLICATION_JSON) //
                        .get(Response.class);

                if(res.getStatus() == 204){
                    continue;
                }
                var b = res.readEntity(Board.class);
                consumer.accept(b);
            }

            EXEC.shutdown();
        });
    }

    public void stop(){
        stop = false;
    }
}
