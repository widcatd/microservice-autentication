package co.com.autentication.r2dbc;

import co.com.autentication.model.constants.Constants;
import co.com.autentication.model.user.User;
import co.com.autentication.model.user.gateways.UserRepository;
import co.com.autentication.r2dbc.entity.UserEntity;
import co.com.autentication.r2dbc.helper.ReactiveAdapterOperations;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Repository
@Transactional
@Slf4j
public class MyReactiveRepositoryAdapter extends ReactiveAdapterOperations<
    User/* change for domain model */,
        UserEntity/* change for adapter model */,
    Long,
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
    public Mono<User> save(User user, String traceId) {
        log.info(Constants.LOG_USER_REPO_START_SAVE, user.getIdentityDocument(), traceId);
        UserEntity userEntity = mapper.map(user, UserEntity.class);
        return repository.save(userEntity)
                .doOnNext(saved ->
                        log.info(Constants.LOG_USER_REPO_SAVED_SUCCESS, saved.getIdUser(), traceId))
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn(Constants.LOG_USER_REPO_ERROR_SAVING, traceId);
                    return Mono.empty();
                }))
                .map(e ->mapper.map(e, User.class));
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return repository.findByEmail(email)
                .map(e -> mapper.map(e, User.class));
    }

    @Override
    public Mono<User> findByIdentityDocument(String identityDocument, String traceId) {
        log.info(Constants.LOG_USER_FIND_BY_DOCUMENT, identityDocument, traceId);
        return repository.findByIdentityDocument(identityDocument)
                .doOnNext(found ->
                        log.info(Constants.LOG_USER_FOUND_BY_DOCUMENT, found.getIdentityDocument(), traceId))
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn(Constants.LOG_IDENTITY_NOT_FOUND, identityDocument, traceId);
                    return Mono.empty();
                }))
                .map(e -> mapper.map(e, User.class));
    }
}
