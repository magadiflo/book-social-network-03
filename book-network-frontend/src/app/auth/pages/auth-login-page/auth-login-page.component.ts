import { Component, OnInit, inject } from '@angular/core';

import { KeycloakService } from '../../../keycloak/keycloak.service';

@Component({
  selector: 'auth-login-page',
  standalone: true,
  imports: [],
  templateUrl: './auth-login-page.component.html',
  styleUrl: './auth-login-page.component.scss'
})
export class AuthLoginPageComponent implements OnInit {

  private _keycloakService = inject(KeycloakService);

  //* Agregamos el async, ya que los métodos que llamamos desde el keycloakService son métodos asíncronos
  async ngOnInit(): Promise<void> {
    await this._keycloakService.init();
    await this._keycloakService.login();
  }

}
