import { Injectable } from '@angular/core';

import Keycloak from 'keycloak-js';

import { UserProfile } from './models/user-profile.model';

@Injectable({
  providedIn: 'root'
})
export class KeycloakService {

  private _keycloak: Keycloak | undefined;
  private _profile: UserProfile | undefined;

  public get keycloak(): Keycloak {
    if (!this._keycloak) {

      this._keycloak = new Keycloak({
        url: 'http://localhost:8181',
        realm: 'book-social-network',
        clientId: 'book-social-network'
      });

    }
    return this._keycloak;
  }

  public get profile(): UserProfile | undefined {
    return this._profile;
  }

  async init(): Promise<void> {
    console.log('Autenticando al usuario...');
    const authenticated = await this.keycloak.init({
      onLoad: 'login-required',
    });

    if (authenticated) {
      console.log('¡Usuario autenticado!');
      this._profile = (await this.keycloak.loadUserProfile()) as UserProfile;
      this._profile.token = this.keycloak.token || '';
    }
  }

  public login(): Promise<void> {
    return this.keycloak.login();
  }

  public logout(): Promise<void> {
    return this.keycloak.logout({
      redirectUri: 'http://localhost:4200'
    });
  }

  public accountManagement(): Promise<void> {
    return this.keycloak.accountManagement();
  }
}
