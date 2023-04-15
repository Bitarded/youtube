import {NgModule} from '@angular/core';
import {AuthModule} from 'angular-auth-oidc-client';


@NgModule({
  imports: [AuthModule.forRoot({
    config: {
      authority: 'https://bitarrded.eu.auth0.com',
      redirectUrl: window.location.origin,
      clientId: 'pipi6fvkub47NpNMTTOHsawUqyw2pc7P',
      scope: 'openid profile offline_access',
      responseType: 'code',
      silentRenew: true,
      useRefreshToken: true,
      secureRoutes: ['http://localhost:8081'],
      customParamsAuthRequest: {
        audience: 'http://localhost:8081'
      }
    }
  })],
  providers: [],
  exports: [AuthModule],
})
export class AuthConfigModule {
}
