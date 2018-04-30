package graphi.examples.simpleblog.repository;

import graphi.annotation.GraphiEndpoint;
import graphi.annotation.GraphiParam;
import graphi.examples.simpleblog.Datasource;
import graphi.examples.simpleblog.entity.Post;

public class PostRepository {

  @GraphiEndpoint(name = "savePost")
  public Post save(@GraphiParam Post post) {
    return Datasource.save("post", post);
  }

}
