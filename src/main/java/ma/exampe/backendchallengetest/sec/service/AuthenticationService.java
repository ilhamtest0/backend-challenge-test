package ma.exampe.backendchallengetest.sec.service;

import ma.exampe.backendchallengetest.sec.payload.request.AuthenticationRequest;
//import ma.exampe.backendchallengetest.sec.payload.request.RegisterRequest;
import ma.exampe.backendchallengetest.sec.payload.response.AuthenticationResponse;

public interface AuthenticationService {
    //AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
}
