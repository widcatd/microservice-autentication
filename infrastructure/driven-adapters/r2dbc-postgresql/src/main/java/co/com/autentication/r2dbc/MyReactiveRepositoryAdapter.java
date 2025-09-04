package co.com.autentication.r2dbc;

import co.com.autentication.model.constants.Constants;
import co.com.autentication.model.exception.RegisterNotFoundException;
import co.com.autentication.model.exceptionusecase.ExceptionUseCaseResponse;
import co.com.autentication.model.user.User;
import co.com.autentication.model.user.gateways.UserRepository;
import co.com.autentication.r2dbc.entity.RoleEntity;
import co.com.autentication.r2dbc.entity.UserEntity;
import co.com.autentication.r2dbc.helper.ReactiveAdapterOperations;
import co.com.autentication.r2dbc.role.RoleReactiveRepository;
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
            private final RoleReactiveRepository roleReactiveRepository;
    public MyReactiveRepositoryAdapter(MyReactiveRepository repository, ObjectMapper mapper, RoleReactiveRepository roleReactiveRepository) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, User.class/* change for domain model */));
        this.roleReactiveRepository = roleReactiveRepository;
    }

    @Override
    public Mono<User> save(User user, String traceId) {
        log.info(Constants.LOG_USER_REPO_START_SAVE, user.getIdentityDocument(), traceId);
        UserEntity userEntity = mapper.map(user, UserEntity.class);
        return roleReactiveRepository.findByIdRole(user.getIdRole())
                .doOnNext(roleEntity -> log.info(Constants.LOG_USER_REPO_SAVED_SUCCESS, user.getIdRole(), traceId))
                .switchIfEmpty(Mono.error(new RegisterNotFoundException(
                        ExceptionUseCaseResponse.ROLE_ID_NOT_FOUND.getCode(),
                        String.format(ExceptionUseCaseResponse.ROLE_ID_NOT_FOUND.getMessage(), user.getIdRole())
                )))
                .flatMap(roleEntity -> {
                    userEntity.setIdRole(roleEntity.getIdRole());
                    return repository.save(userEntity);
                })
                .doOnSuccess(saved ->
                        log.info(Constants.LOG_USER_REPO_SAVED_SUCCESS, saved.getIdUser(), traceId))
                .map(e -> mapper.map(e,User.class))
                .doOnError(error ->
                        log.error(Constants.LOG_USER_REPO_ERROR_SAVING, error.getMessage(), traceId, error));
    }

    @Override
    public Mono<User> findByEmail(String email, String traceId) {
        log.info(Constants.LOG_USER_FIND_BY_EMAIL, email, traceId);
        return repository.findByEmail(email)
                .flatMap(user -> roleReactiveRepository.findByIdRole(user.getIdRole())
                        .map(userRole -> {
                            user.setRole(userRole.getName());
                            return user;
                        })
                )
                .map(e -> mapper.map(e, User.class))
                .doOnError(ex ->
                        log.error(Constants.LOG_USER_ERROR_FIND_BY_EMAIL, email, traceId, ex.getMessage()));

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

    @Override
    public Mono<User> signUp(User user) {
        return null;
    }
}
