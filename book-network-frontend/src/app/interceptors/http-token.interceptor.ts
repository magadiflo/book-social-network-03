import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';

import { KeycloakService } from '../keycloak/keycloak.service';

export const httpTokenInterceptor: HttpInterceptorFn = (req, next) => {
  const keycloakService = inject(KeycloakService);
  const token: string | undefined = keycloakService.keycloak.token;

  let reqClone = req;

  if (token) {
    reqClone = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${token}`)
    });
  }

  return next(reqClone);
};
