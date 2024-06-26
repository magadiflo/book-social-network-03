import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class KeycloakService {

  async init() {
    console.log('Inicializando Keycloak');
  }

}
