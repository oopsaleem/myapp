/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import EmpUpdate from './emp-update.vue';
import EmpService from './emp.service';
import AlertService from '@/shared/alert/alert.service';

type EmpUpdateComponentType = InstanceType<typeof EmpUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const empSample = { id: 'ABC' };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<EmpUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Emp Management Update Component', () => {
    let comp: EmpUpdateComponentType;
    let empServiceStub: SinonStubbedInstance<EmpService>;

    beforeEach(() => {
      route = {};
      empServiceStub = sinon.createStubInstance<EmpService>(EmpService);
      empServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

      alertService = new AlertService({
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          empService: () => empServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(EmpUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.emp = empSample;
        empServiceStub.update.resolves(empSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(empServiceStub.update.calledWith(empSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        empServiceStub.create.resolves(entity);
        const wrapper = shallowMount(EmpUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.emp = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(empServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        empServiceStub.find.resolves(empSample);
        empServiceStub.retrieve.resolves([empSample]);

        // WHEN
        route = {
          params: {
            empId: '' + empSample.id,
          },
        };
        const wrapper = shallowMount(EmpUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.emp).toMatchObject(empSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        empServiceStub.find.resolves(empSample);
        const wrapper = shallowMount(EmpUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
