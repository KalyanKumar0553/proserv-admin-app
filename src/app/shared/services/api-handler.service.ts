import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams, HttpRequest, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { ResponseErrors } from '../constants/constants.enum';

@Injectable({
  providedIn: 'root'
})
export class ApiHandlerService {
  protected baseUrl!: string;

  constructor(
    private httpClient: HttpClient
  ) { }

  formulateApiUrl(apiEndpoint: string): string {
    return this.baseUrl + apiEndpoint;
  }

  /**
   * Execute HTTP GET method
   * @param apiEndpoint API endpoint
   */
  get<T>(apiEndpoint: string): Observable<any> {
    return this.httpClient
      .get<T>(this.formulateApiUrl(apiEndpoint))
      .pipe(
        map((res: T) => this.handleResponse(res)),
        catchError((error: HttpErrorResponse) => this.handleError(error))
      );
  }

  /**
   * Execute HTTP GET method with query parameters
   * @param apiEndpoint API endpoint
   * @param requestParams query parameters
   */
  getWithParams(apiEndpoint: string, requestParams: HttpParams): Observable<any> {
    requestParams = requestParams.append('observe', 'response');
    return this.httpClient
      .get(this.formulateApiUrl(apiEndpoint), { params: requestParams })
      .pipe(
        map((res) => this.handleResponse(res)),
        catchError((error: HttpErrorResponse) => this.handleError(error))
      );
  }

  /**
   * Execute HTTP GET method with headers
   * @param apiEndpoint API endpoint
   * @param headers headers
   */
  getWithHeaders(apiEndpoint: string, headers: HttpHeaders): Observable<any> {
    return this.httpClient
      .get(this.formulateApiUrl(apiEndpoint), {
        headers: headers,
        responseType: 'text' as 'json'
      })
      .pipe(
        map((res) => this.handleResponse(res)),
        catchError((error: HttpErrorResponse) => this.handleError(error))
      );
  }

  /**
   * Execute HTTP GET method with response type as blob
   * @param baseUrl API base URL
   * @param apiEndpoint API endpoint
   */
  getBlobData(apiEndpoint: string): Observable<any> {
    return this.httpClient
      .get(this.formulateApiUrl(apiEndpoint), {
        observe: 'response',
        responseType: 'blob'
      })
      .pipe(
        map((res: HttpResponse<any>) => this.handleResponse(res)),
        catchError((error: HttpErrorResponse) => this.handleError(error))
      );
  }

  /**
   * Execute HTTP GET method with response type as blob
   * @param apiEndpoint API endpoint
   * @param requestParam query parameters
   */
  getBlobDataWithParam(apiEndpoint: string, requestParam: HttpParams): Observable<any> {
    return this.httpClient
      .get(this.formulateApiUrl(apiEndpoint), {
        observe: 'response',
        responseType: 'blob',
        params: requestParam
      })
      .pipe(
        map((res: HttpResponse<any>) => this.handleResponse(res)),
        catchError((error: HttpErrorResponse) => this.handleError(error))
      );
  }

  /**
   * Execute HTTP GET method with response type as blob
   * @param apiEndpoint API endpoint
   */
  getText(apiEndpoint: string): Observable<any> {
    return this.httpClient
      .get(this.formulateApiUrl(apiEndpoint), {
        observe: 'response',
        responseType: 'text'
      })
      .pipe(
        map((res: HttpResponse<any>) => this.handleResponse(res)),
        catchError((error: HttpErrorResponse) => this.handleError(error))
      );
  }

  /**
   * Execute HTTP POST method with request body
   * @param apiEndpoint API endpoint
   * @param data API request payload
   */
  save(apiEndpoint: string, data: any): Observable<any> {
    return this.httpClient
      .post(this.formulateApiUrl(apiEndpoint), data)
      .pipe(
        map((res) => this.handleResponse(res)),
        catchError((error: HttpErrorResponse) => this.handleError(error))
      );
  }

  saveWithHeaders(apiEndPoint: string, data: any, requestHeaders: HttpHeaders): Observable<any> {
    return this.httpClient
      .post(this.formulateApiUrl(apiEndPoint), data, { headers: requestHeaders })
      .pipe(
        map((res) => this.handleResponse(res)),
        catchError((error: HttpErrorResponse) => this.handleError(error))
      );
  }

  fileUpload(url: string, formData: any, method: string, headers: HttpHeaders, queryParams?: HttpParams): Observable<any> {
    url = this.formulateApiUrl(url);
    const params = queryParams || new HttpParams();
    const req = new HttpRequest(method, url, formData, { reportProgress: true, headers, params });
    return this.httpClient.request(req);
  }

  /**
   * Execute HTTP POST method with request body and query params
   * @param apiEndpoint API endpoint
   * @param data API request payload
   */
  saveWithParams(apiEndpoint: string, data: any, requestParams: HttpParams): Observable<any> {
    return this.httpClient
      .post(this.formulateApiUrl(apiEndpoint), data, {
        params: requestParams
      })
      .pipe(
        map((res) => this.handleResponse(res)),
        catchError((error: HttpErrorResponse) => this.handleError(error))
      );
  }

  /**
   * Execute HTTP PUT method with request body
   * @param apiEndpoint API endpoint
   * @param data API request payload
   */
  update(apiEndpoint: string, data: any): Observable<any> {
    return this.httpClient
      .put(this.formulateApiUrl(apiEndpoint), data)
      .pipe(
        map((res) => this.handleResponse(res)),
        catchError((error: HttpErrorResponse) => this.handleError(error))
      );
  }

  updateWithHeaders(apiEndPoint: string, data: any, requestHeaders: HttpHeaders): Observable<any> {
    return this.httpClient
      .put(this.formulateApiUrl(apiEndPoint), data, { headers: requestHeaders })
      .pipe(
        map((res) => this.handleResponse(res)),
        catchError((error: HttpErrorResponse) => this.handleError(error))
      );
  }

  /**
   * Execute HTTP DELETE method
   * @param apiEndpoint API endpoint
   */
  delete(apiEndpoint: string): Observable<any> {
    return this.httpClient
      .delete(this.formulateApiUrl(apiEndpoint))
      .pipe(
        map((res) => this.handleResponse(res)),
        catchError((error: HttpErrorResponse) => this.handleError(error))
      );
  }

  /**
   * Execute HTTP DELETE method
   * @param apiEndpoint API endpoint
   * @param requestParam query parameters
   */
  deleteWithParams(apiEndpoint: string, requestParams: HttpParams): Observable<any> {
    return this.httpClient
      .delete(this.formulateApiUrl(apiEndpoint), {
        params: requestParams
      })
      .pipe(
        map((res) => this.handleResponse(res)),
        catchError((error: HttpErrorResponse) => this.handleError(error))
      );
  }

  /**
   * Execute HTTP DELETE method with request body
   * @param apiEndpoint API endpoint
   * @param data API request payload
   */
  deleteWithRequestBody(apiEndpoint: string, data: any): Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
      body: data
    };
    return this.httpClient
      .delete(
        this.formulateApiUrl(apiEndpoint),
        httpOptions
      )
      .pipe(
        map((res) => this.handleResponse(res)),
        catchError((error: HttpErrorResponse) => this.handleError(error))
      );
  }

  patch(apiEndpoint: string, data: any): Observable<any> {
    return this.httpClient
      .patch(this.formulateApiUrl(apiEndpoint), data)
      .pipe(
        map((res) => this.handleResponse(res)),
        catchError((error: HttpErrorResponse) => this.handleError(error))
      );
  }


  /**
   * Handles api response
   */
  handleResponse(response: any): any {
    if (response == null || response.status === 204) {
      return {};
    } else {
      if(response?.headers?.get('Content-Type')=='application/json') {
        if(response) {
          if(response.body)
            return response.body;
          if(response.result)
            return response.result;
        }
      }
      return response;
    }
  }

  /**
   * Handles api error
   */
  handleError(error: HttpErrorResponse): Observable<never> {
    if (error.error instanceof Object) {
      error.error.status = error.status;
    }
    if (error.status === 401) {
      return throwError(error.error);
    }
    if(error.status === 0 && error.error instanceof ProgressEvent) {
      return throwError({ "error" : ResponseErrors.NETWORK_DISCONNECTED });
    } 
    return error.error ? throwError(error.error) : throwError(error);
  }
}
