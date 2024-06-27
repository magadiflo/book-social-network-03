import { Injectable } from '@angular/core';

import Keycloak from 'keycloak-js';

@Injectable({
  providedIn: 'root'
})
export class KeycloakService {

  private _keycloak: Keycloak | undefined;

  public get keycloak() {
    if (!this._keycloak) {

      this._keycloak = new Keycloak({
        url: 'http://localhost:8181',
        realm: 'book-social-network',
        clientId: 'book-social-network'
      });

    }
    return this._keycloak;
  }

  async init() {
    console.log('Autenticando al usuario...');
    const authenticated = await this.keycloak.init({
      onLoad: 'login-required',
    });

    if (authenticated) {
      console.log('¡Usuario autenticado!');
    }
  }

}
