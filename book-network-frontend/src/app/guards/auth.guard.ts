import { inject } from '@angular/core';
import { CanActivateFn, CanMatchFn, Router } from '@angular/router';

import { KeycloakService } from '../keycloak/keycloak.service';

export const canMatchAuthGuard: CanMatchFn = (route, segments) => {
  console.log('Ejecutnado canMatchAuthGuard()');
  return checkAuthStatus();
}

export const canActivateAuthGuard: CanActivateFn = (route, state) => {
  console.log('Ejecutnado canActivateAuthGuard()');
  return checkAuthStatus();
};

const checkAuthStatus = () => {
  const keycloakService = inject(KeycloakService);
  const router = inject(Router);

  if (keycloakService.keycloak.isTokenExpired()) {
    router.navigate(['/auth', 'login']);
    return false;
  }

  return true;
}
