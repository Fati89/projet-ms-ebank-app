import { HttpInterceptorFn } from '@angular/common/http';
import {inject} from '@angular/core';
import {finalize} from 'rxjs';
import {Loading} from '../services/loading';

export const loadingInterceptor: HttpInterceptorFn = (req, next) => {
  const loadingService = inject(Loading);
  loadingService.setLoading(true);
  return next(req).pipe(finalize(() => {
    loadingService.setLoading(false);
  }));
};
