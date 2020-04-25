# Istio Service Mesh

## About Application
This is a basic crud operation web project with springboot and angularJs as frontend and DynamoDB as persistance NoSql database. It is deployed on EKS cluster. Additional microservices are added to make service mesh a bit complex

## Prerequisites
* Running EKS cluster. Refer to projects for
1. [Terraform](https://github.com/anujshukla-1987/k8s-terraform)
2. [Cloudformation](https://github.com/anujshukla-1987/k8s-cloudformation)

## Installing Application
All the commands are executed from root of the project.

* ### Installing Istio

 * ###### Installing ISTIO CRD's
 ```bash
 kubectl apply -f ./istio-install/istio-init.yaml
 ```
 Wait for all CRD's to be in completed state.Check completion status from below command
 ```bash
 kubectl get pods --namespace=istio-system
 ```

 * ###### Installing Istio Control Plane components
 ```bash
 kubectl apply -f ./istio-install/istio-install.yaml
 ```
 Verify all control plane components are ion either completed or running state.
 ```bash
 kubectl get pods --namespace=istio-system
```
Output should be something like this
```
grafana-6bb6bcf99f-q6xln                  1/1     Running     0          51m
istio-citadel-7d49f4cb9b-zpfc5            1/1     Running     0          51m
istio-galley-54dffbcfb6-m8jdw             1/1     Running     0          51m
istio-grafana-post-install-1.4.3-klkqt    0/1     Completed   0          51m
istio-init-crd-10-1.4.3-6mfd2             0/1     Completed   0          51m
istio-init-crd-11-1.4.3-c75qt             0/1     Completed   0          51m
istio-init-crd-14-1.4.3-kxvmb             0/1     Completed   0          51m
istio-pilot-547964d586-xh2gb              2/2     Running     2          51m
istio-policy-7585bffc57-kr4xw             2/2     Running     1          51m
istio-security-post-install-1.4.3-pq76f   0/1     Completed   0          51m
istio-sidecar-injector-7c8887877f-8nmz2   1/1     Running     0          51m
istio-telemetry-78d6dfff5c-9jcmv          2/2     Running     1          51m
istio-tracing-56c7f85df7-5pbrm            1/1     Running     0          51m
kiali-7b5c8f79d8-kc4cl                    1/1     Running     0          51m
prometheus-74d8b55f54-shw9r               1/1     Running     0          51m
```
Istio setup is done. Now we can deploy application as part of service mesh

* ### Installing Application
 Istio is a non-invasive framework so application does not need to have any changes to accomodate istio other than one. To enable istio to inject envoy proxies into each pod automatically we need to label our namespace with `istio-injection: enabled` . Any pod created with the lableled namespace will automatically add a sidecar envoy proxy.

 * ###### Installing DNS controller
```bash
kubectl apply -f ./deployments/external-dns-controller.yaml
```
 * ###### Installing Microservice application components
 ```bash
 kubectl apply -f ./deployments/application-deployment.yaml
 ```
 * ###### Installing Istio Ingress Gateway
 ```bash
 kubectl apply -f ./deployments/istio-ingress-gateway.yaml
 ```
 Check status of the pods
 ```bash
 kubectl get pods --namespace=eks-poc-istio
 ```
 ```
 NAME                                                READY   STATUS    RESTARTS   AGE
dynamodb-deployment-9cd988846-2zp2h                 2/2     Running   0          73m
nginx-frontend-deployment-7d54b7cf59-gn26d          2/2     Running   0          73m
nodejs-backend-deployment-7c9b676ccb-5rhzx          2/2     Running   0          73m
python-backend-deployment-77c9d7bc55-pf9d5          2/2     Running   0          73m
springboot-backend-api-deployment-ccd595dfd-vz94q   2/2     Running   0          73m
```
Notice that each pod has 2 running containers , 1 application container and one sidecar envoy proxy of istio.

## Service Mesh Visualization
Istio comes inbuilt with various logging and visualization componennts which can be accessed as below
   * ###### Kiali
   Kiali is an very powerful open source service mesh visualization , observation and performance analysis dashboard
   ```bash
   istioctl dashboard kiali
   ```
   It offers amazing istio features within dashboards like
   * Weighted Routing
   * Suspending traffic
   * Canary deployments
   * Container Logs

   And many more. Check https://kiali.io/

   * ###### Grafana
   Inbuilt grafana dashboard for performance monitoring.
   ```bash
   istioctl dashboard grafana
   ```
   * ###### Jaeger-UI
   Inbuilt jaeger dashboard for request tracing.
   ```bash
   istioctl dashboard jaeger
   ```
