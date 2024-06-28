import { Component, inject } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { SlicePipe } from '@angular/common';

import { KeycloakService } from '../../../keycloak/keycloak.service';

@Component({
  selector: 'books-menu',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, SlicePipe],
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.scss'
})
export class MenuComponent {

  private _keycloakService = inject(KeycloakService);

  public get fullName(): string {
    return this._keycloakService.profile?.firstName || '';
  }

  //* Agregamos el async, ya que los métodos que llamamos desde el keycloakService son métodos asíncronos
  async logout(): Promise<void> {
    this._keycloakService.logout();
  }

  async accountManagement(): Promise<void> {
    this._keycloakService.accountManagement();
  }

}
