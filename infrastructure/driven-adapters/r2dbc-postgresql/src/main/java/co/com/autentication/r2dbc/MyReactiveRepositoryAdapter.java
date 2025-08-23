package co.com.autentication.r2dbc;

import co.com.autentication.model.user.User;
import co.com.autentication.model.user.gateways.UserRepository;
import co.com.autentication.r2dbc.entity.UserEntity;
import co.com.autentication.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class MyReactiveRepositoryAdapter extends ReactiveAdapterOperations<
    User/* change for domain model */,
        UserEntity/* change for adapter model */,
    String,
    MyReactiveRepository
> implements UserRepository {
    public MyReactiveRepositoryAdapter(MyReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, User.class/* change for domain model */));
    }

    @Override
    public Mono<User> save(User user) {
        UserEntity userEntity = mapper.map(user, UserEntity.class);
        return repository.save(userEntity)
                .map(e ->mapper.map(e, User.class));
    }
}
