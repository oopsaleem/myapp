import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import EmpService from './emp.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { type IEmp, Emp } from '@/shared/model/emp.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'EmpUpdate',
  setup() {
    const empService = inject('empService', () => new EmpService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const emp: Ref<IEmp> = ref(new Emp());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveEmp = async empId => {
      try {
        const res = await empService().find(empId);
        emp.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.empId) {
      retrieveEmp(route.params.empId);
    }

    const validations = useValidation();
    const validationRules = {
      firstName: {},
      lastName: {},
    };
    const v$ = useVuelidate(validationRules, emp as any);
    v$.value.$validate();

    return {
      empService,
      alertService,
      emp,
      previousState,
      isSaving,
      currentLanguage,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.emp.id) {
        this.empService()
          .update(this.emp)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo('A Emp is updated with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.empService()
          .create(this.emp)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess('A Emp is created with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
